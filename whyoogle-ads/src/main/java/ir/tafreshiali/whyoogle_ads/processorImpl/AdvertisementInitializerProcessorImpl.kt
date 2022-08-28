package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Application
import android.content.Context
import android.view.ViewGroup
import ir.ayantech.pishkhancore.core.PishkhanCore
import ir.tafreshiali.whyoogle_ads.extension.checkAdvertisementStatus
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.AdvertisementInitializerProcessor

class AdvertisementInitializerProcessorImpl : AdvertisementInitializerProcessor {
    override fun appAdvertisementGeneralProcessor(
        activityContext: Context,
        application: Application,
        adView: ViewGroup,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit,
        onNativeAdLoaded: () -> Unit
    ) {
        PishkhanCore.getAppConfigAdvertisement(activityContext) {
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
        }
    }
}