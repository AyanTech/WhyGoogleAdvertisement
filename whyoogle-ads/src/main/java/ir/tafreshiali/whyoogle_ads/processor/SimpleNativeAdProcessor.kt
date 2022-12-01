package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import ir.ayantech.ayannetworking.api.AyanApi
import ir.tafreshiali.whyoogle_ads.R

interface SimpleNativeAdProcessor {
    fun simpleNativeAdProcessor(
        activityContext: AppCompatActivity,
        context: Application,
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        @LayoutRes adiveryNativeLayoutId: Int = R.layout.adivery_native_ad,
        @LayoutRes admobNativeLayoutId: Int = R.layout.admob_simple_native_ad,
        @LayoutRes ayanNativeLayoutId: Int,
        ayanApi: AyanApi,
        appGeneralAdStatus: Boolean,
        appNativeAdStatus: Boolean,
        adView: ViewGroup
    )
}