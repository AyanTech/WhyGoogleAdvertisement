package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import android.content.Context
import android.view.ViewGroup

interface AdvertisementInitializerProcessor {
    fun appAdvertisementGeneralProcessor(
        activityContext: Context,
        application: Application,
        adView: ViewGroup,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit,
        onNativeAdLoaded: () -> Unit
    )
}