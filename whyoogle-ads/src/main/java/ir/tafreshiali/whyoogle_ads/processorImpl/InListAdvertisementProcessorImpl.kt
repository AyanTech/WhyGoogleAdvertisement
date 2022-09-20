package ir.tafreshiali.whyoogle_ads.processorImpl

import android.content.Context
import android.view.ViewGroup
import com.adivery.sdk.AdiveryNativeAdView
import ir.tafreshiali.whyoogle_ads.AdvertisementCore
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.extension.handleApplicationNativeAdvertisement
import ir.tafreshiali.whyoogle_ads.extension.loadAdiveryNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView
import ir.tafreshiali.whyoogle_ads.processor.InListAdvertisementProcessor

class InListAdvertisementProcessorImpl : InListAdvertisementProcessor {

    /**
     * usually we want to load and show native ads as the second item in the list for this use case we use [listNativeAdProcessor]. At the end, it checks if the list value is empty or not.
     * @param [activityContext] [appGeneralAdStatus] [itemList]
     * @param updateListItems when ever the native ad loaded successfully this lambda function triggers, usually in this block we should update the our list .(call the [androidx.recyclerview.widget.ListAdapter.notifyItemInserted] and etc)
     * */

    override fun listNativeAdProcessor(
        activityContext: Context,
        appGeneralAdStatus: Boolean,
        itemList: ArrayList<Any>,
        updateListItems: () -> Unit
    ): Boolean {
        if (appGeneralAdStatus) {
            if (itemList.isNotEmpty()) {
                handleApplicationNativeAdvertisement(
                    loadAdiveryNativeView = {
                        loadAdiveryNativeAdvertisementView(
                            context = activityContext,
                            adiveryNativeLayoutId = R.layout.adivery_card_ad,
                            onAdLoaded = { adiveryView ->

                                if (itemList.find { it is AdiveryNativeAdView } == null) {

                                    itemList.add(1, adiveryView)

                                    updateListItems()
                                }

                            }
                        )
                    },
                    loadAdmobNativeView = {
                        AdvertisementCore.admobNativeAdvertisement?.let { admobNativeAd ->
                            loadAdmobNativeAdvertisementView(
                                context = activityContext,
                                admobNativeAdvertisement = admobNativeAd,
                                admobNativeLayoutId = R.layout.admob_native_ad,
                                onAdLoaded = { admobAdView ->
                                    if (itemList.find { it is ViewGroup } == null) {
                                        itemList.add(1, admobAdView)

                                        updateListItems()
                                    }
                                }
                            )
                        }
                    }
                )
            }
            return itemList.isEmpty()
        }
        return false
    }
}
