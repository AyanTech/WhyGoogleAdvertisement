package ir.tafreshiali.whyoogle_ads.processorImpl

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.adivery.sdk.AdiveryNativeAdView
import ir.ayantech.ayannetworking.api.AyanApi
import ir.tafreshiali.whyoogle_ads.AdvertisementCore
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementInput
import ir.tafreshiali.whyoogle_ads.extension.handleApplicationNativeAdvertisement
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAyanNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.InListAdvertisementProcessor

class InListAdvertisementProcessorImpl : InListAdvertisementProcessor {

    /**
     * usually we want to load and show native ads as the second item in the list for this use case we use [listNativeAdProcessor]. At the end, it checks if the list value is empty or not.
     * @param [context] [appGeneralAdStatus] [itemList]
     * @param updateListItems when ever the native ad loaded successfully this lambda function triggers, usually in this block we should update the our list .(call the [androidx.recyclerview.widget.ListAdapter.notifyItemInserted] and etc)
     * */

    override fun listNativeAdProcessor(
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        ayanNativeAdvertisementInput: AyanCustomAdvertisementInput,
        @LayoutRes adiveryNativeLayoutId: Int,
        @LayoutRes admobNativeLayoutId: Int,
        @LayoutRes ayanNativeLayoutId: Int,
        ayanApi: AyanApi,
        activityContext: AppCompatActivity,
        appGeneralAdStatus: Boolean,
        itemList: MutableList<Any>,
        adViewInListIndex: Int,
        updateListItems: (MutableList<Any>) -> Unit
    ): Boolean {
        if (appGeneralAdStatus) {
            if (itemList.isNotEmpty()) {
                handleApplicationNativeAdvertisement(
                    loadAdiveryNativeView = {
                        if (adiveryNativeAdUnit.isNotEmpty())
                            loadAdiveryNativeAdvertisementView(
                                adiveryNativeAdUnit = adiveryNativeAdUnit,
                                context = activityContext,
                                adiveryNativeLayoutId = adiveryNativeLayoutId,
                                onAdLoaded = { adiveryView ->

                                    if (itemList.find { it is AdiveryNativeAdView } == null) {

                                        itemList.add(adViewInListIndex, adiveryView)

                                        updateListItems(itemList)
                                    }

                                }
                            )
                    },
                    loadAdmobNativeView = {
                        if (admobNativeAdvertisementId.isNotEmpty())
                            AdvertisementCore.admobAdvertisement.loadNativeAdLoader(
                                context = activityContext.application,
                                admobNativeAdvertisementId = admobNativeAdvertisementId,
                                onNativeAdLoaded = { admobNativeAd ->
                                    loadAdmobNativeAdvertisementView(
                                        context = activityContext.application,
                                        admobNativeAdvertisement = admobNativeAd,
                                        admobNativeLayoutId = admobNativeLayoutId,
                                        onAdLoaded = { admobAdView ->
                                            if (itemList.find { it is ViewGroup } == null) {
                                                itemList.add(adViewInListIndex, admobAdView)

                                                updateListItems(itemList)
                                            }
                                        }
                                    )
                                },
                                onNativeAdFailed = {
                                    Log.d(
                                        "Admob",
                                        "Loading Admob Native Advertisement in a list failed"
                                    )
                                }
                            )
                    },
                    loadAyanNativeView = {
                        if (ayanNativeAdvertisementInput.Container.isNotEmpty())
                            AdvertisementCore.ayanAdvertisement.loadAyanCustomNativeAdvertisement(
                                ayanApi = ayanApi,
                                input = ayanNativeAdvertisementInput,
                                callBack = { ayanAdCustomModel ->
                                    loadAyanNativeAdvertisementView(
                                        activityContext = activityContext,
                                        ayanNativeLayoutId = ayanNativeLayoutId,
                                        ayanCustomAdvertisementModel = ayanAdCustomModel,
                                        onAdLoaded = { ayanAdView ->
                                            if (itemList.find { it is ViewGroup } == null) {
                                                itemList.add(adViewInListIndex, ayanAdView)

                                                updateListItems(itemList)
                                            }
                                        },
                                        onAdFailed = {
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
            return itemList.isEmpty()
        }
        return false
    }
}
