package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure

interface AdvertisementInitializerProcessor {
    fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        changeStatus: OnChangeStatus? = null,
        failure: OnFailure? = null,
        onSourceInitialized: (Boolean) -> Unit,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit
    )
}