package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Application
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.ayantech.whygoogle.helper.toJsonString
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdvertisementResponse
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.ApplicationAdvertisementType
import ir.tafreshiali.whyoogle_ads.extension.checkAdvertisementStatus
import ir.tafreshiali.whyoogle_ads.extension.findRelatedAdUnit
import ir.tafreshiali.whyoogle_ads.extension.getAppConfigAdvertisement
import ir.tafreshiali.whyoogle_ads.processor.AdvertisementInitializerProcessor

class AdvertisementInitializerProcessorImpl : AdvertisementInitializerProcessor {

    /**
     * Used to send ads request to the server and initialize the advertisement
     * Important Note = [appAdvertisementGeneralProcessor] should calls once in a [androidx.appcompat.app.AppCompatActivity.onCreate] or in a [android.app.Application.onCreate] but before call this function,
     * you should ensure that you would initialize the [ir.ayantech.ayannetworking.api.AyanApi]
     * @param application
     * @param [changeStatus] [failure] for state handling in upstreams.
     * @param updateAppGeneralAdvertisementStatus a lambda function that has [Boolean] value witch determine that app should shows the ads or not.*/

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
                AdvertisementResponse.appAdvertisementResponse = this.toJsonString()

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
}