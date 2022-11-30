package ir.tafreshiali.whyoogle_ads.processor

import android.app.Activity
import android.content.Context

interface InterstitialAdProcessor {
    fun loadInterstitialAdvertisement(
        activity: Activity,
        appGeneralAdStatus: Boolean,
        admobInterstitialAdUnit: String,
        adiveryInterstitialAdUnit: String,
    )
}