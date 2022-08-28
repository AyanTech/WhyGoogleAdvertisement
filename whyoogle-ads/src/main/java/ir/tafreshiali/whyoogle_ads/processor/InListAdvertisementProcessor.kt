package ir.tafreshiali.whyoogle_ads.processor

import android.content.Context

interface InListAdvertisementProcessor {
    fun listNativeAdProcessor(
        activityContext: Context,
        appGeneralAdStatus: Boolean,
        itemList: ArrayList<Any>,
        updateListItems: () -> Unit
    ): Boolean
}