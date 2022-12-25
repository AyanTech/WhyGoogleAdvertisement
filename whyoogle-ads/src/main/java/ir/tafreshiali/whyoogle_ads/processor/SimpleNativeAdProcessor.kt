package ir.tafreshiali.whyoogle_ads.processor

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import ir.ayantech.ayannetworking.api.AyanApi
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementInput

interface SimpleNativeAdProcessor {
    fun simpleNativeAdProcessor(
        activityContext: AppCompatActivity,
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        ayanNativeAdvertisementInput: AyanCustomAdvertisementInput,
        @LayoutRes adiveryNativeLayoutId: Int = R.layout.adivery_native_ad,
        @LayoutRes admobNativeLayoutId: Int = R.layout.admob_simple_native_ad,
        @LayoutRes ayanNativeLayoutId: Int = R.layout.ayan_simple_native_ad,
        ayanApi: AyanApi,
        appGeneralAdStatus: Boolean,
        adView: ViewGroup,
        onAdLoaded: () -> Unit
    )
}