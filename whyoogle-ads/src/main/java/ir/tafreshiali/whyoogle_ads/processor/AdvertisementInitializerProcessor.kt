package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import android.content.Context
import android.view.ViewGroup
import ir.ayantech.ayannetworking.api.AyanApi

interface AdvertisementInitializerProcessor {
    fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        adView: ViewGroup,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit,
        onNativeAdLoaded: () -> Unit
    )
}