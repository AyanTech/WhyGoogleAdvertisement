package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Application
import android.view.ViewGroup
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.tafreshiali.whyoogle_ads.extension.checkAdvertisementStatus
import ir.tafreshiali.whyoogle_ads.extension.getAppConfigAdvertisement
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.AdvertisementInitializerProcessor


class AdvertisementInitializerProcessorImpl : AdvertisementInitializerProcessor {

    /**
     * Used to send ads request to the server and initialize the advertisement
     * Important Note = [appAdvertisementGeneralProcessor] should calls once in a [androidx.appcompat.app.AppCompatActivity.onCreate] or in a [android.app.Application.onCreate] but before call this function,
     * you should ensure that you would initialize the [ir.ayantech.ayannetworking.api.AyanApi]
     * @param application
     * @param adView the view that we want to load the ad in it.
     * @param [changeStatus] [failure] for state handling in upstreams.
     * @param updateAppGeneralAdvertisementStatus a lambda function that has [Boolean] value witch determine that app should shows the ads or not.
     * @param onNativeAdLoaded when ever the advertisement loaded successfully the lambda function triggers*/

    override fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        adView: ViewGroup,
        changeStatus: OnChangeStatus?,
        failure: OnFailure?,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit,
        onNativeAdLoaded: () -> Unit
    ) {
        ayanApi.getAppConfigAdvertisement(
            callBack = {
                if (it.Active) {
                    it.checkAdvertisementStatus(
                        application = application,
                        callback = updateAppGeneralAdvertisementStatus,
                        handleAdiveryNativeAdvertisement = {
                            adView.loadAdiveryNativeAdvertisementView(
                                adiveryNativeLayoutId = ir.tafreshiali.whyoogle_ads.R.layout.adivery_native_ad,
                                onAdLoaded = onNativeAdLoaded
                            )
                        },
                        showAdmobNativeAdvertisement = {
                            ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobNativeAdvertisement?.let {
                                adView.loadAdmobNativeAdvertisementView(
                                    nativeAd = it,
                                    admobNativeLayoutId = ir.tafreshiali.whyoogle_ads.R.layout.admob_simple_native_ad,
                                    onViewReady = onNativeAdLoaded
                                )
                            }
                        }
                    )
                }
            },
            changeStatus = changeStatus,
            failure = failure
        )
    }
}