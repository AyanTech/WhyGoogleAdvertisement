package ir.tafreshiali.whyoogle_ads.extension

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.children
import com.adivery.sdk.AdiveryNativeAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.textview.MaterialTextView
import ir.ayantech.advertisement.core.AdvertisementCore
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.ayantech.pishkhancore.helper.loadFromString
import ir.ayantech.pishkhancore.model.AppConfigAdvertisementOutput
import ir.ayantech.pishkhancore.model.Source
import ir.ayantech.whygoogle.adapter.MultiViewTypeViewHolder
import ir.ayantech.whygoogle.helper.*
import ir.tafreshiali.whyoogle_ads.AdvertisementEndpoint
import ir.tafreshiali.whyoogle_ads.AyanAdvertisement
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementModel
import ir.tafreshiali.whyoogle_ads.constance.ApplicationCommonAdvertisementKeys
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.ApplicationAdvertisementType

/**
 * getAppConfigAdvertisement an extension function for getting application advertisement info
 * @param callBack of type lambda function that triggers us that the server has responded to our request
 * @param [onChangeStatus] [onFailure] for state handling in upstreams*/

fun AyanApi.getAppConfigAdvertisement(
    callBack: (AppConfigAdvertisementOutput) -> Unit,
    onChangeStatus: OnChangeStatus? = null,
    onFailure: OnFailure? = null
) {
    call<AppConfigAdvertisementOutput>(endPoint = AdvertisementEndpoint.AppConfigAdvertisement) {
        useCommonFailureCallback = false
        useCommonChangeStatusCallback = false
        success { it?.let(callBack) }
        onChangeStatus?.let { changeStatusCallback(it) }
        onFailure?.let { failure(it) }
    }
}


/**
 * checkAdvertisementStatus Based on [AppConfigAdvertisementOutput]
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * @param callback a lambda function for doing some operation on the advertisement activation state*/
fun AppConfigAdvertisementOutput.checkAdvertisementStatus(
    adiveryAppKey: String,
    admobInterstitialAdUnit: String,
    adiveryInterstitialAdUnit: String,
    application: Application,
    ayanAdvertisement: AyanAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.ayanAdvertisement,
    onSourceInitialized: (Boolean) -> Unit,
    callback: (Boolean) -> Unit,
) {

    if (this.Active) {
        ApplicationAdvertisementType.appAdvertisementType =
            this.Sources.firstOrNull { it.Key == ApplicationAdvertisementType.APPLICATION_ADVERTISEMENT_SOURCE }?.Value
                ?: return

        callback.invoke(this.Active)

        when (ApplicationAdvertisementType.appAdvertisementType) {

            ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY -> {
                initializeAdmobAdvertisement(
                    admobInterstitialAdUnit = admobInterstitialAdUnit,
                    ayanAdvertisement = ayanAdvertisement,
                    admobMainInitializationStatus = onSourceInitialized,
                    handleAdmobInterstitialAdvertisement = { admobInterstitialAd ->
                        ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                            admobInterstitialAd = admobInterstitialAd
                        )


                        ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobInterstitialAdvertisement?.addAdmobInterstitialCallback(
                            admobInterstitialAdUnit = admobInterstitialAdUnit,
                            application = application,
                            admobAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobAdvertisement,
                        )
                    }
                )
            }

            ApplicationCommonAdvertisementKeys.ADIVERY_ADVERTISEMENT_KEY -> {
                ayanAdvertisement.loadAdiveryAdvertisement(
                    adiveryInterstitialAdUnit = adiveryInterstitialAdUnit,
                    adiveryAppKey = adiveryAppKey
                )
                onSourceInitialized(true)
            }

            ApplicationCommonAdvertisementKeys.AYAN_ADVERTISEMENT -> {
                onSourceInitialized(true)
            }
        }
    }
}


fun AppConfigAdvertisementOutput.findRelatedAdUnit(key: String): String =
    this.Sources.firstOrNull { it.Key == key }?.Value
        ?: ""

fun List<Source>.findRelatedAdUnit(key: String): String =
    this.firstOrNull { it.Key == key }?.Value
        ?: ""


/**
 * initializeAdmobAdvertisement Based On [AppConfigAdvertisementOutput]
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * @param handleAdmobInterstitialAdvertisement
 * @param admobInterstitialAdUnit
 * */

fun initializeAdmobAdvertisement(
    admobInterstitialAdUnit: String,
    ayanAdvertisement: AyanAdvertisement,
    admobMainInitializationStatus: (Boolean) -> Unit,
    handleAdmobInterstitialAdvertisement: (InterstitialAd?) -> Unit
) {
    ayanAdvertisement.loadAdmobAdvertisement(
        admobInterstitialAdUnit = admobInterstitialAdUnit,
        admobMainInitializationStatus = { status ->
            if (!status) {
                ApplicationAdvertisementType.reset()
            } else {
                ApplicationAdvertisementType.appAdvertisementType =
                    ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY
                ApplicationAdvertisementType.appInterstitialAdvertisementType =
                    ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY
                ApplicationAdvertisementType.appNativeAdvertisementType =
                    ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY
            }
            admobMainInitializationStatus(status)
        },
        onInterstitialAdLoaded = {

            handleAdmobInterstitialAdvertisement(it)

            ApplicationAdvertisementType.appAdvertisementType =
                ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY

            ApplicationAdvertisementType.appInterstitialAdvertisementType =
                ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY
        },
        onInterstitialFailed = {
            handleAdmobInterstitialAdvertisement(null)
            ApplicationAdvertisementType.appInterstitialAdvertisementType = ""
        }
    )
}


/**
 * For Each Loading of InterstitialAd we need to add a call back
 * @param application of type [Application]
 * @param admobAdvertisement of type [AdmobAdvertisement]
 * @param admobInterstitialAdUnit of type [String]*/

fun InterstitialAd.addAdmobInterstitialCallback(
    admobInterstitialAdUnit: String,
    application: Application,
    admobAdvertisement: AdmobAdvertisement
) {
    admobAdvertisement.addInterstitialCallbacks(
        interstitialAd = this,
        onAdShowedFullScreenContent = {
            ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                null
            )
            Log.d("Admob", "onAdShowedFullScreenContent")
            admobAdvertisement.loadInterstitial(
                context = application,
                onInterstitialAdLoaded = { admobInterstitialAd ->
                    ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                        admobInterstitialAd
                    )
                },
                onInterstitialFailed = {
                    Log.d(
                        "Switch",
                        "Admob Interstitial Advertisement Fail Switch To Adivery"
                    )

                    ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                        null
                    )
                    ApplicationAdvertisementType.appInterstitialAdvertisementType =
                        ApplicationCommonAdvertisementKeys.ADIVERY_ADVERTISEMENT_KEY
                },
                admobInitializeAdvertisementId = admobInterstitialAdUnit
            )
        },
        onAdDismissedFullScreenContent = {
            Log.d("Admob", "onAdDismissedFullScreenContent")
        },

        onAdFailedToShowFullScreenContent = {
            Log.d("Admob", "onAdFailedToShowFullScreenContent")

        }
    )
}


/**Loading Admob Native Advertisement To the [ViewGroup]
 * **Important Note = Each Native Admob Advertisement Loaded With Specific [NativeAd] So We Should Load That [NativeAd] With Unique Ad unit**
 * @param nativeAd of type [NativeAd] Needed For putting the advertisement information to the views
 * @param admobNativeLayoutId of type [LayoutRes] the id of created layout in (project / library ) res folder
 * @param layoutParams of type [ViewGroup.LayoutParams] for defining the [ViewGroup] width and height
 * @param onViewReady a lambda function for updating upstreams to react when ever advertisement loaded */
fun ViewGroup.loadAdmobNativeAdvertisementView(
    nativeAd: NativeAd,
    @LayoutRes admobNativeLayoutId: Int,
    layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ),
    onViewReady: () -> Unit
) {
    trying {

        // Inflate a layout and add it to the parent ViewGroup.
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(admobNativeLayoutId, null, true) as NativeAdView

        // Locate the view that will hold the headline, set its text, and use the
        // NativeAdView's headlineView property to register it.
        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        headlineView.text = nativeAd.headline
        headlineView.isSelected = true
        adView.headlineView = headlineView


        val adAppIcon = adView.findViewById<ImageView>(R.id.ad_app_icon)
        adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        adView.iconView = adAppIcon


        val adCallToAction = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
        adCallToAction.text = nativeAd.callToAction
        adView.callToActionView = adCallToAction

        adView.setNativeAd(nativeAd)

        removeAllViews()

        addView(
            adView,
            layoutParams
        )

        onViewReady()
    }
}

fun ViewGroup.loadAyanNativeAdvertisementView(
    activityContext: AppCompatActivity,
    @LayoutRes ayanNativeLayoutId: Int,
    layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ),
    ayanCustomAdvertisementModel: AyanCustomAdvertisementModel,
    onAdLoaded: () -> Unit,
    onAdFailed: () -> Unit
) {
    trying {
        // Inflate a layout and add it to the parent ViewGroup.
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(ayanNativeLayoutId, null, true) as LinearLayout

        val header = adView.findViewById<MaterialTextView>(R.id.ayan_ad_title)

        header?.let {
            it.text = ayanCustomAdvertisementModel.Title
            it.isSelected = true
        } ?: run {
            Log.d("AYAN_ADVERTISEMENT", "cant replace the title in the passed layout")
            return@trying
        }

        val adIcon = adView.findViewById<AppCompatImageView>(R.id.ayan_ad_app_icon)
        adIcon?.loadFromString(ayanCustomAdvertisementModel.Banner) ?: run {
            Log.d("AYAN_ADVERTISEMENT", "cant load the banner link in the passed layout")
            return@trying
        }

        val adButtonTitle = adView.findViewById<AppCompatButton>(R.id.ayan_ad_call_to_action)
        adButtonTitle?.let {
            it.text = ayanCustomAdvertisementModel.ButtonRedirectName
            it.setOnClickListener {
                ayanCustomAdvertisementModel.ButtonRedirectLink.openUrl(context = activityContext)
            }
        } ?: run {
            Log.d("AYAN_ADVERTISEMENT", "cant load the button content in the passed layout")
            return@trying
        }
        if (adButtonTitle.isNull() || adIcon.isNull() || header.isNull()) {
            onAdFailed()
            return@trying
        } else {
            addView(
                adView,
                layoutParams
            )
            onAdLoaded()
        }
    }
}


fun loadAyanNativeAdvertisementView(
    activityContext: AppCompatActivity,
    @LayoutRes ayanNativeLayoutId: Int,
    ayanCustomAdvertisementModel: AyanCustomAdvertisementModel,
    layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ),
    onAdLoaded: (ViewGroup) -> Unit,
    onAdFailed: () -> Unit
) {
    LinearLayout(activityContext).let { ayanNativeAdContainer ->
        ayanNativeAdContainer.loadAyanNativeAdvertisementView(
            activityContext = activityContext,
            ayanNativeLayoutId = ayanNativeLayoutId,
            layoutParams = layoutParams,
            ayanCustomAdvertisementModel = ayanCustomAdvertisementModel,
            onAdLoaded = { onAdLoaded(ayanNativeAdContainer) },
            onAdFailed = onAdFailed
        )
    }
}


/**
 * Loading Adivery Native Advertisement To the [ViewGroup]
 * @param [onAdLoaded] a lambda function for updating upstreams to react when ever advertisement loaded
 * @param [adiveryNativeLayoutId] of type [LayoutRes] the id of created layout in (project / library ) res folder */
fun ViewGroup.loadAdiveryNativeAdvertisementView(
    adiveryNativeAdUnit: String,
    onAdLoaded: () -> Unit,
    @LayoutRes adiveryNativeLayoutId: Int
) {
    this.addView(
        AdvertisementCore.requestNativeAds(
            context = context,
            layoutId = adiveryNativeLayoutId,
            customAdUnit = adiveryNativeAdUnit
        ) {
            trying {
                findViewById<TextView>(R.id.adivery_headline).isSelected = true
                onAdLoaded()
            }
        }
    )
    trying {
        this.setOnClickListener {
            this.findViewById<AppCompatButton>(R.id.adivery_call_to_action)
                .performClick()
        }
    }
}


/**
 * Loading Adivery Native Advertisement To the [AdiveryNativeAdView]
 * @param [onAdLoaded] a lambda function for updating upstreams to react when ever advertisement loaded
 * @param [adiveryNativeLayoutId] of type [LayoutRes] the id of created layout in (project / library ) res folder */

fun loadAdiveryNativeAdvertisementView(
    context: Context,
    adiveryNativeAdUnit: String,
    onAdLoaded: (AdiveryNativeAdView) -> Unit,
    @LayoutRes adiveryNativeLayoutId: Int
) {
    var adView: AdiveryNativeAdView? = null

    adView = AdvertisementCore.requestNativeAds(
        context = context,
        layoutId = adiveryNativeLayoutId,
        customAdUnit = adiveryNativeAdUnit
    ) {
        trying {
            adView?.let {
                it.findViewById<TextView>(R.id.adivery_headline).isSelected =
                    true

                val adiveryButton =
                    it.findViewById<AppCompatButton>(R.id.adivery_call_to_action)

                adiveryButton.setOnClickListener {
                    adiveryButton.performClick()
                }
            }
            adView?.let { onAdLoaded(it) }
        }
    }
}


/** Loading Admob Native Advertisement To The [LinearLayout]
 * @param context of type [Context] for instantiating [ViewGroup]
 * @param admobNativeLayoutId of type [LayoutRes] the id of created layout in (project / library ) res folder
 * @param admobNativeAdvertisement of type [NativeAd] needed for [loadAdmobNativeAdvertisementView]
 * @param onAdLoaded a lambda function for updating upstreams to react when ever advertisement loaded*/
fun loadAdmobNativeAdvertisementView(
    context: Context,
    @LayoutRes admobNativeLayoutId: Int,
    admobNativeAdvertisement: NativeAd,
    onAdLoaded: (ViewGroup) -> Unit
) {
    LinearLayout(context).let { admobNativeContainer ->
        admobNativeContainer.loadAdmobNativeAdvertisementView(
            nativeAd = admobNativeAdvertisement,
            admobNativeLayoutId = admobNativeLayoutId,
            onViewReady = { onAdLoaded(admobNativeContainer) }
        )
    }
}


/**
 * handling application native  advertisement
 * @param [applicationAdvertisementType]  for handling different type of native advertisement
 * @param [loadAdiveryNativeView] [loadAdmobNativeView] a lambda functions for updating upstreams to react when ever interstitial advertisement requested */

fun handleApplicationNativeAdvertisement(
    applicationAdvertisementType: String = ApplicationAdvertisementType.appAdvertisementType,
    loadAdiveryNativeView: () -> Unit,
    loadAdmobNativeView: () -> Unit,
    loadAyanNativeView: () -> Unit
) {

    when (applicationAdvertisementType) {
        ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY -> {
            loadAdmobNativeView()
        }

        ApplicationCommonAdvertisementKeys.ADIVERY_ADVERTISEMENT_KEY -> {
            loadAdiveryNativeView()
        }

        ApplicationCommonAdvertisementKeys.AYAN_ADVERTISEMENT -> {
            loadAyanNativeView()
        }
    }
}


/** handling application interstitial advertisement
 * @param activity we need activity for loading admob advertisement
 * @param admobAdvertisement for loading admob advertisement
 * @param [admobInterstitialAdUnit] [adiveryInterstitialAdUnit] for loading app interstitial advertisement
 * @param [loadAdmobInterstitialAdvertisement] [loadAdiveryInterstitialAdvertisement] a lambda functions for updating upstreams to react when ever interstitial advertisement requested  */

fun showApplicationInterstitialAdvertisement(
    activity: Activity,
    admobInterstitialAdUnit: String,
    adiveryInterstitialAdUnit: String,
    admobAdvertisement: AdmobAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobAdvertisement,
    loadAdmobInterstitialAdvertisement: () -> Unit = {},
    loadAdiveryInterstitialAdvertisement: () -> Unit = {}
) {
    when (ApplicationAdvertisementType.appAdvertisementType) {

        ApplicationCommonAdvertisementKeys.ADMOB_ADVERTISEMENT_KEY -> {

            ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobInterstitialAdvertisement?.let { admobInterstitialAd ->
                admobInterstitialAd.addAdmobInterstitialCallback(
                    application = activity.application,
                    admobAdvertisement = admobAdvertisement,
                    admobInterstitialAdUnit = admobInterstitialAdUnit
                )
                admobInterstitialAd.show(activity)

                loadAdmobInterstitialAdvertisement()
            }
        }

        ApplicationCommonAdvertisementKeys.ADIVERY_ADVERTISEMENT_KEY -> {
            AdvertisementCore.showInterstitialAds(customAdUnit = adiveryInterstitialAdUnit)
            loadAdiveryInterstitialAdvertisement()
        }
    }
}


/**
 * For Handling The Clicks On Whole Item In List
 * Important Note : Should Be Used In The [androidx.recyclerview.widget.RecyclerView.onCreateViewHolder] */
fun MultiViewTypeViewHolder<Any>.registerClickForNativeAdvertisement() {
    this.registerClickListener(this.itemView) { rootView ->
        if (rootView is LinearLayout) {
            when (rootView.children.firstOrNull()) {

                is AdiveryNativeAdView -> {
                    rootView.findViewById<AppCompatButton>(ir.ayantech.pishkhancore.R.id.adivery_call_to_action)
                        .performClick()
                }

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




