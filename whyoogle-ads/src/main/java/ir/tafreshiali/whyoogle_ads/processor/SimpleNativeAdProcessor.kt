package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import ir.tafreshiali.whyoogle_ads.R

interface SimpleNativeAdProcessor {
    fun simpleNativeAdProcessor(
        context: Application,
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        @LayoutRes adiveryNativeLayoutId: Int = R.layout.adivery_native_ad,
        @LayoutRes admobNativeLayoutId: Int = R.layout.admob_simple_native_ad,
        appGeneralAdStatus: Boolean,
        appNativeAdStatus: Boolean,
        adView: ViewGroup
    )
}