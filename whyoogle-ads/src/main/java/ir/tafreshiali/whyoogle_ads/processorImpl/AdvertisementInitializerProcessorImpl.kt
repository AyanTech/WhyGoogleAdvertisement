package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.ayantech.whygoogle.helper.SimpleCallBack
import ir.ayantech.whygoogle.helper.changeVisibility
import ir.ayantech.whygoogle.helper.toJsonString
import ir.tafreshiali.whyoogle_ads.BuildConfig
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdvertisementResponse
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.ApplicationAdvertisementType
import ir.tafreshiali.whyoogle_ads.extension.checkAdvertisementStatus
import ir.tafreshiali.whyoogle_ads.extension.findRelatedAdUnit
import ir.tafreshiali.whyoogle_ads.extension.getAppConfigAdvertisement
import ir.tafreshiali.whyoogle_ads.processor.AdvertisementInitializerProcessor
import java.util.concurrent.atomic.AtomicBoolean

class AdvertisementInitializerProcessorImpl : AdvertisementInitializerProcessor {

    override var consentInformation: ConsentInformation? = null

    @Deprecated(message = "This method is deprecated")
    override fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        changeStatus: OnChangeStatus?,
        failure: OnFailure?,
        onSourceInitialized: (Boolean) -> Unit,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit
    ) {
        ayanApi.getAppConfigAdvertisement(
            callBack = {

                //Save The Advertisement Response After Each Request And Use It In Other Parts
                AdvertisementResponse.appAdvertisementResponse = it.toJsonString()

                if (it.Active) {
                    it.checkAdvertisementStatus(
                        application = application,
                        onSourceInitialized = onSourceInitialized,
                        callback = updateAppGeneralAdvertisementStatus,
                        adiveryInterstitialAdUnit = it.findRelatedAdUnit(key = ApplicationAdvertisementType.AD_INTERSTITIAL_ADIVERY_KEY),
                        admobInterstitialAdUnit = it.findRelatedAdUnit(key = ApplicationAdvertisementType.AD_INTERSTITIAL_ADMOB_KEY),
                        adiveryAppKey = it.findRelatedAdUnit(key = ApplicationAdvertisementType.APP_ADIVERY_KEY)
                    )
                }
            },
            onChangeStatus = changeStatus,
            onFailure = failure
        )
    }

    /**
     * Used to send ads request to the server and initialize the advertisement
     * Important Note = [appAdvertisementGeneralProcessor] should calls once in a [androidx.appcompat.app.AppCompatActivity.onCreate] or in a [android.app.Application.onCreate] but before call this function,
     * you should ensure that you would initialize the [ir.ayantech.ayannetworking.api.AyanApi]
     * @param application
     * @param [changeStatus] [failure] for state handling in upstreams.
     * @param updateAppGeneralAdvertisementStatus a lambda function that has [Boolean] value witch determine that app should shows the ads or not.*/

    override fun appAdvertisementGeneralProcessor(
        activity: Activity,
        ayanApi: AyanApi,
        application: Application,
        checkGDPR: Boolean,
        changeStatus: OnChangeStatus?,
        failure: OnFailure?,
        onSourceInitialized: (Boolean) -> Unit,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit
    ) {
//        checkGDPR(activity) {
        ayanApi.getAppConfigAdvertisement(
            callBack = {

                //Save The Advertisement Response After Each Request And Use It In Other Parts
                AdvertisementResponse.appAdvertisementResponse = it.toJsonString()

                if (it.Active) {
                    consentInformation = it.checkAdvertisementStatus(
                        activity = activity,
                        application = application,
                        onSourceInitialized = onSourceInitialized,
                        callback = updateAppGeneralAdvertisementStatus,
                        adiveryInterstitialAdUnit = it.findRelatedAdUnit(key = ApplicationAdvertisementType.AD_INTERSTITIAL_ADIVERY_KEY),
                        admobInterstitialAdUnit = it.findRelatedAdUnit(key = ApplicationAdvertisementType.AD_INTERSTITIAL_ADMOB_KEY),
                        adiveryAppKey = it.findRelatedAdUnit(key = ApplicationAdvertisementType.APP_ADIVERY_KEY)
                    )
                }
            },
            onChangeStatus = changeStatus,
            onFailure = failure
        )
//        }
    }

    private fun checkGDPR(activity: Activity, callback: SimpleCallBack) {

        val params = ConsentRequestParameters.Builder()
            .setConsentDebugSettings(
                if (BuildConfig.DEBUG) {
                    ConsentDebugSettings.Builder(activity)
                        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                        .setForceTesting(true)
                        .build()
                } else null
            )
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(activity)

//        if (BuildConfig.DEBUG)
//            consentInformation?.reset()

        consentInformation?.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    // Consent gathering failed.
                    Log.w(
                        "dghdfgjhj", String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                    if (consentInformation?.canRequestAds() == true) {
                        initializeMobileAdsSdk(activity, callback)
                    }

//                    binding.privacyIv.changeVisibility(isPrivacyOptionsRequired)
                }
            },
            { requestConsentError ->
                // Consent gathering failed.
                Log.w(
                    "dghdfgjhj", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation?.canRequestAds() == true) {
            initializeMobileAdsSdk(activity, callback)
        }
    }

    override fun initializeMobileAdsSdk(activity: Activity, callback: SimpleCallBack) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(activity)

        callback()
    }
}