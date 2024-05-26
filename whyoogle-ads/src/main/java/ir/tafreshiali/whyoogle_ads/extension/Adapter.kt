package ir.tafreshiali.whyoogle_ads.extension

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
//import com.adivery.sdk.AdiveryNativeAdView
import com.google.android.gms.ads.nativead.NativeAdView
import ir.ayantech.whygoogle.adapter.MultiViewTypeViewHolder
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.databinding.RowNativeAdInListPlaceHolderBinding


/**
 * For Handling The Clicks On Whole Item In List
 * Important Note : Should Be Used In The [androidx.recyclerview.widget.RecyclerView.onCreateViewHolder]
 *  @param analyticsEvent for enabling upstreams to send an analytics events */
fun MultiViewTypeViewHolder<Any>.handleClickForNativeAdvertisement(analyticsEvent: (() -> Unit)? = null) {
    this.registerClickListener(this.itemView) { rootView ->
        analyticsEvent?.invoke()
        if (rootView is LinearLayout) {
            when (rootView.children.firstOrNull()) {

//                is AdiveryNativeAdView -> {
//                    rootView.findViewById<AppCompatButton>(R.id.adivery_call_to_action)
//                        .performClick()
//                }

                is NativeAdView -> {
                    rootView.findViewById<AppCompatButton>(ir.tafreshiali.whyoogle_ads.R.id.ad_call_to_action)
                }

                is LinearLayout -> {
                    rootView.findViewById<AppCompatButton>(ir.tafreshiali.whyoogle_ads.R.id.ayan_ad_call_to_action)
                        .performClick()
                }
            }
        }
    }
}

/**
 * For Handling The Clicks On Whole Item In List
 * Important Note : Should Be Used In The [androidx.recyclerview.widget.RecyclerView.onCreateViewHolder]
 * @param analyticsEvent for enabling upstreams to send an analytics events */
fun MultiViewTypeViewHolder<Any>.registerClickForNativeAdvertisement(
    analyticsEvent: (() -> Unit)? = null
) {
    when (this.viewBinding) {
        is RowNativeAdInListPlaceHolderBinding -> {
            this.handleClickForNativeAdvertisement(analyticsEvent = analyticsEvent)
        }
    }
}


/**
 * Loading Native Advertisement In List
 * Important Note : Should Be Used In The [androidx.recyclerview.widget.RecyclerView.onBindViewHolder]
 * @param advertisementItem of type [Any] you should pass the ad item like below =
 *
 * loadAdViewInAdapter(advertisementItem= itemsToView[position])
 *
 * */
 fun MultiViewTypeViewHolder<Any>.loadAdViewInAdapter(
    advertisementItem: Any
) {
    (this.viewBinding as? RowNativeAdInListPlaceHolderBinding)?.let { nativeAdBinding ->
        (advertisementItem as ViewGroup).let { adView ->
            //The specified child already has a parent. You must call removeView() on the child's parent first
            nativeAdBinding.nativeAdView.removeAllViews()
            nativeAdBinding.nativeAdView.addView(adView)
        }
    }
}



