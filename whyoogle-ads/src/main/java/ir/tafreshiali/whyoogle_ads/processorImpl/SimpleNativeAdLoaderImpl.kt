package ir.tafreshiali.whyoogle_ads.processorImpl

import android.view.ViewGroup
import ir.ayantech.whygoogle.helper.makeVisible
import ir.tafreshiali.whyoogle_ads.AdvertisementCore
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.extension.handleApplicationNativeAdvertisement
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.SimpleNativeAdProcessor

class SimpleNativeAdLoaderImpl : SimpleNativeAdProcessor {

    /**
     * used when ever we want to load native advertisement in a specific view.
     * @param [appGeneralAdStatus] [appNativeAdStatus] of type [Boolean] that determine if ad should load or not
     * @param adView of type [ViewGroup] the view that ad load in it , it can be defined in xml or kotlin code.*/
    override fun simpleNativeAdProcessor(
        appGeneralAdStatus: Boolean,
        appNativeAdStatus: Boolean,
        adView: ViewGroup
    ) {
        if (appGeneralAdStatus && appNativeAdStatus)
            handleApplicationNativeAdvertisement(
                loadAdiveryNativeView = {
                    adView.loadAdiveryNativeAdvertisementView(
                        adiveryNativeLayoutId = R.layout.adivery_native_ad,
                        onAdLoaded = { adView.makeVisible() })
                },
                loadAdmobNativeView = {
                    AdvertisementCore.admobNativeAdvertisement?.let { admobNativeAd ->
                        adView.loadAdmobNativeAdvertisementView(
                            nativeAd = admobNativeAd,
                            admobNativeLayoutId = R.layout.admob_simple_native_ad,
                            onViewReady = {
                                adView.makeVisible()
                            }
                        )
                    }
                }
            )
    }

}