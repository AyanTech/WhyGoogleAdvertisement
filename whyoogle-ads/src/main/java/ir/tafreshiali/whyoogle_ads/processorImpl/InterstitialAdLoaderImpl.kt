package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Activity
import ir.tafreshiali.whyoogle_ads.extension.showApplicationInterstitialAdvertisement
import ir.tafreshiali.whyoogle_ads.processor.InterstitialAdProcessor

class InterstitialAdLoaderImpl : InterstitialAdProcessor {
    override fun loadInterstitialAdvertisement(activity: Activity, appGeneralAdStatus: Boolean) {
        if (appGeneralAdStatus)
            showApplicationInterstitialAdvertisement(activity = activity)
    }
}