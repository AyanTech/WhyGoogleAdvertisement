package ir.tafreshiali.whyoogle_ads.processor

import android.app.Application
import androidx.annotation.LayoutRes
import ir.tafreshiali.whyoogle_ads.R

interface InListAdvertisementProcessor {
    fun listNativeAdProcessor(
        adiveryNativeAdUnit: String,
        admobNativeAdvertisementId: String,
        @LayoutRes adiveryNativeLayoutId: Int = R.layout.adivery_native_ad,
        @LayoutRes admobNativeLayoutId: Int = R.layout.admob_simple_native_ad,
        context: Application,
        appGeneralAdStatus: Boolean,
        itemList: ArrayList<Any>,
        updateListItems: () -> Unit
    ): Boolean
}