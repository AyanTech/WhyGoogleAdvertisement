package ir.tafreshiali.whyoogle_ads.processor

import android.view.ViewGroup

interface SimpleNativeAdProcessor {
    fun simpleNativeAdProcessor(
        appGeneralAdStatus: Boolean,
        appNativeAdStatus: Boolean,
        adView: ViewGroup
    )
}