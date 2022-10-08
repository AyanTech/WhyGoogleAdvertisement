package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import android.content.Context
import android.view.ViewGroup
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure

interface AdvertisementInitializerProcessor {
    fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        adView: ViewGroup,
        changeStatus: OnChangeStatus,
        failure: OnFailure,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit,
        onNativeAdLoaded: () -> Unit
    )
}