package ir.tafreshiali.whyoogle_ads.processorImpl

import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.whygoogle.helper.makeGone
import ir.ayantech.whygoogle.helper.makeVisible
import ir.tafreshiali.whyoogle_ads.AdvertisementCore
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementInput
import ir.tafreshiali.whyoogle_ads.extension.handleApplicationNativeAdvertisement
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAyanNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.SimpleNativeAdProcessor

class SimpleNativeAdLoaderImpl : SimpleNativeAdProcessor {

    /**
     * used when ever we want to load native advertisement in a specific view.
     * @param [appGeneralAdStatus] [appNativeAdStatus] of type [Boolean] that determine if ad should load or not
     * @param adView of type [ViewGroup] the view that ad load in it , it can be defined in xml or kotlin code.*/
    override fun simpleNativeAdProcessor(
        activityContext: AppCompatActivity,
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        ayanNativeAdvertisementInput: AyanCustomAdvertisementInput,
        adiveryNativeLayoutId: Int,
        admobNativeLayoutId: Int,
        ayanNativeLayoutId: Int,
        ayanApi: AyanApi,
        appGeneralAdStatus: Boolean,
        adView: ViewGroup,
        onAdLoaded: (() -> Unit)?
    ) {
        if (appGeneralAdStatus)
            handleApplicationNativeAdvertisement(
                loadAdiveryNativeView = {
                    if (adiveryNativeAdUnit.isNotEmpty())
                        adView.loadAdiveryNativeAdvertisementView(
                            adiveryNativeAdUnit = adiveryNativeAdUnit,
                            adiveryNativeLayoutId = adiveryNativeLayoutId,
                            onAdLoaded = {
                                adView.makeVisible()
                                onAdLoaded?.invoke()
                            })
                },
                loadAdmobNativeView = {
                    if (admobNativeAdvertisementId.isNotEmpty())
                        AdvertisementCore.admobAdvertisement.loadNativeAdLoader(
                            context = activityContext.application,
                            admobNativeAdvertisementId = admobNativeAdvertisementId,
                            onNativeAdLoaded = { admobNativeAd ->
                                adView.loadAdmobNativeAdvertisementView(
                                    nativeAd = admobNativeAd,
                                    admobNativeLayoutId = admobNativeLayoutId,
                                    onViewReady = {
                                        adView.makeVisible()
                                        onAdLoaded?.invoke()
                                    }
                                )
                            },
                            onNativeAdFailed = {
                                adView.makeGone()
                                Log.d("Admob", "Loading Native Ad Is Failed")
                            }
                        )
                },
                loadAyanNativeView = {
                    if (ayanNativeAdvertisementInput.Container.isNotEmpty())
                        AdvertisementCore.ayanAdvertisement.loadAyanCustomNativeAdvertisement(
                            ayanApi = ayanApi,
                            input = ayanNativeAdvertisementInput,
                            callBack = { ayanAdCustomModel ->
                                adView.loadAyanNativeAdvertisementView(
                                    activityContext = activityContext,
                                    ayanNativeLayoutId = ayanNativeLayoutId,
                                    ayanCustomAdvertisementModel = ayanAdCustomModel,
                                    onAdLoaded = {
                                        adView.makeVisible()
                                        onAdLoaded?.invoke()
                                    },
                                    onAdFailed = {
                                        adView.makeGone()
                                        Log.d(
                                            "Admob",
                                            "Loading Ayan Native Advertisement in a list failed"
                                        )
                                    }
                                )
                            }
                        )
                }
            )
    }
}