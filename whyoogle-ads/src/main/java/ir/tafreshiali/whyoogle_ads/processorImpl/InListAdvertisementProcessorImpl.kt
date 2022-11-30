package ir.tafreshiali.whyoogle_ads.processorImpl

import android.app.Application
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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
     * @param [context] [appGeneralAdStatus] [itemList]
     * @param updateListItems when ever the native ad loaded successfully this lambda function triggers, usually in this block we should update the our list .(call the [androidx.recyclerview.widget.ListAdapter.notifyItemInserted] and etc)
     * */

    override fun listNativeAdProcessor(
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        @LayoutRes adiveryNativeLayoutId: Int,
        @LayoutRes admobNativeLayoutId: Int,
        context: Application,
        appGeneralAdStatus: Boolean,
        itemList: ArrayList<Any>,
        updateListItems: () -> Unit
    ): Boolean {
        if (appGeneralAdStatus) {
            if (itemList.isNotEmpty()) {
                handleApplicationNativeAdvertisement(
                    loadAdiveryNativeView = {
                        loadAdiveryNativeAdvertisementView(
                            adiveryNativeAdUnit = adiveryNativeAdUnit,
                            context = context,
                            adiveryNativeLayoutId = adiveryNativeLayoutId,
                            onAdLoaded = { adiveryView ->

                                if (itemList.find { it is AdiveryNativeAdView } == null) {

                                    itemList.add(1, adiveryView)

                                    updateListItems()
                                }

                            }
                        )
                    },
                    loadAdmobNativeView = {

                        AdvertisementCore.admobAdvertisement.loadNativeAdLoader(
                            context = context,
                            admobNativeAdvertisementId = admobNativeAdvertisementId,
                            onNativeAdLoaded = { admobNativeAd ->
                                loadAdmobNativeAdvertisementView(
                                    context = context,
                                    admobNativeAdvertisement = admobNativeAd,
                                    admobNativeLayoutId = R.layout.admob_native_ad,
                                    onAdLoaded = { admobAdView ->
                                        if (itemList.find { it is ViewGroup } == null) {
                                            itemList.add(1, admobAdView)

                                            updateListItems()
                                        }
                                    }
                                )
                            },
                            onNativeAdFailed = {
                                Log.d("Admob", "Loading Admob Native Advertisement in a list failed")
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
