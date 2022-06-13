package ir.tafreshiali.whyoogle_ads.extension

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import ir.ayantech.advertisement.core.AdvertisementCore
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.adivery.sdk.AdiveryNativeAdView
import com.google.android.gms.ads.nativead.NativeAdView
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.pishkhancore.model.AppConfigAdvertisementOutput
import ir.ayantech.whygoogle.helper.trying
import ir.tafreshiali.whyoogle_ads.AdvertisementEndpoint
import ir.tafreshiali.whyoogle_ads.AyanAdvertisement
import ir.tafreshiali.whyoogle_ads.R
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdiveryAdvertisementKey
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdmobAdvertisementKey
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.ApplicationAdvertisementType

/**
 * getAppConfigAdvertisement an extension function for getting application advertisement info
 * @param callBack of type lambda function that triggers us that the server has responded to our request*/
fun AyanApi.getAppConfigAdvertisement(
    callBack: (AppConfigAdvertisementOutput?) -> Unit
) {
    simpleCall<AppConfigAdvertisementOutput>(
        endPoint = AdvertisementEndpoint.AppConfigAdvertisement,
        onSuccess = { response ->
            callBack.invoke(response)
        }
    )
}


/**
 * checkAdvertisementStatus Based on [AppConfigAdvertisementOutput]
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * @param callback a lambda function for doing some operation on the advertisement activation state
 * @param handleAdiveryNativeAdvertisement
 * @param handleAdmobInterstitialAdvertisement
 * @param handleAdmobNativeAdvertisement
 * @param showAdmobNativeAdvertisement*/
fun AppConfigAdvertisementOutput.checkAdvertisementStatus(
    ayanAdvertisement: AyanAdvertisement,
    callback: (Boolean) -> Unit,
    handleAdiveryNativeAdvertisement: () -> Unit,
    handleAdmobInterstitialAdvertisement: (InterstitialAd?) -> Unit,
    handleAdmobNativeAdvertisement: (NativeAd?) -> Unit,
    showAdmobNativeAdvertisement: () -> Unit
) {
    if (this.Active) {
        ApplicationAdvertisementType.appAdvertisementType =
            this.Sources.first { it.Key == "source" }.Value
        when (ApplicationAdvertisementType.appAdvertisementType) {

            "admob" -> {
                this.initializeAdmobAdvertisement(
                    ayanAdvertisement = ayanAdvertisement,
                    handleAdiveryNativeAdvertisement = handleAdiveryNativeAdvertisement,
                    handleAdmobInterstitialAdvertisement = handleAdmobInterstitialAdvertisement,
                    handleAdmobNativeAdvertisement = handleAdmobNativeAdvertisement,
                    showAdmobNativeAdvertisement = showAdmobNativeAdvertisement
                )
            }

            "adivery" -> {
                this.initializeAdiveryAdvertisement(ayanAdvertisement)
                handleAdiveryNativeAdvertisement()
            }
        }
    }
    callback.invoke(this.Active)
}


/**
 * initializeAdiveryAdvertisement Based on [AppConfigAdvertisementOutput]
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * */
fun AppConfigAdvertisementOutput.initializeAdiveryAdvertisement(ayanAdvertisement: AyanAdvertisement) {
    ayanAdvertisement.saveAdiveryAdvertisementKeys(
        appKey = this.Sources.first { it.Key == AdiveryAdvertisementKey.APP_AD_KEY }.Value,
        interstitialAdUnitID = this.Sources.first { it.Key == AdiveryAdvertisementKey.AD_INTERSTITIAL_KEY }.Value,
        nativeAdUnitID = this.Sources.first { it.Key == AdiveryAdvertisementKey.AD_NATIVE_KEY }.Value,
        bannerAdUnitID = this.Sources.first { it.Key == AdiveryAdvertisementKey.AD_BANNER_KEY }.Value
    )
    ayanAdvertisement.loadAdiveryAdvertisement(adiveryAdvertisementProperties = ayanAdvertisement.readAdiveryAdvertisementProperties())

}


/**
 * initializeAdmobAdvertisement Based On [AppConfigAdvertisementOutput]
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * @param handleAdiveryNativeAdvertisement
 * @param handleAdmobInterstitialAdvertisement
 * @param handleAdmobNativeAdvertisement
 * @param showAdmobNativeAdvertisement
 * */

fun AppConfigAdvertisementOutput.initializeAdmobAdvertisement(
    ayanAdvertisement: AyanAdvertisement,
    handleAdiveryNativeAdvertisement: () -> Unit,
    handleAdmobInterstitialAdvertisement: (InterstitialAd?) -> Unit,
    handleAdmobNativeAdvertisement: (NativeAd?) -> Unit,
    showAdmobNativeAdvertisement: () -> Unit

) {
    this.initializeAdiveryAdvertisement(ayanAdvertisement = ayanAdvertisement)
    ayanAdvertisement.saveAdmobAdvertisementKeys(
        nativeAdvertisementId = this.Sources.first { it.Key == AdmobAdvertisementKey.AD_NATIVE_KEY }.Value,
        interstitialAdvertisementId = this.Sources.first { it.Key == AdmobAdvertisementKey.AD_INTERSTITIAL_KEY }.Value
    )

    ayanAdvertisement.loadAdmobAdvertisement(
        onMainAdmobInitializationFailed = {
            ApplicationAdvertisementType.appAdvertisementType = "adivery"
            ApplicationAdvertisementType.appInterstitialAdvertisementType = "adivery"
            ApplicationAdvertisementType.appNativeAdvertisementType = "adivery"
            handleAdiveryNativeAdvertisement()
        },
        onInterstitialAdLoaded = {

            handleAdmobInterstitialAdvertisement(it)

            ApplicationAdvertisementType.appInterstitialAdvertisementType = "admob"
        },
        onInterstitialFailed = {
            handleAdmobInterstitialAdvertisement(null)
            ApplicationAdvertisementType.appInterstitialAdvertisementType = "adivery"
        },
        onNativeAdFailed = {
            handleAdmobNativeAdvertisement(null)
            ApplicationAdvertisementType.appNativeAdvertisementType = "adivery"
            handleAdiveryNativeAdvertisement()
        },
        onNativeAdLoaded = {
            handleAdmobNativeAdvertisement(it)
            ApplicationAdvertisementType.appNativeAdvertisementType = "admob"
            showAdmobNativeAdvertisement()
        }
    )
}


/**
 * For Each Loading of InterstitialAd we need to add a call back
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * @param application of type [Application]
 * @param admobAdvertisement of type [AdmobAdvertisement]*/

fun InterstitialAd.addAdmobInterstitialCallback(
    ayanAdvertisement: AyanAdvertisement,
    application: Application,
    admobAdvertisement: AdmobAdvertisement
) {
    admobAdvertisement.addInterstitialCallbacks(
        interstitialAd = this,
        onAdShowedFullScreenContent = {
            ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                null
            )
            Log.d("TAG", "onAdShowedFullScreenContent")
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
                        "adivery"
                },
                admobInitializeAdvertisementId = ayanAdvertisement.readAdmobAdvertisementProperties().interstitialAdvertisementId
            )
        },
        onAdDismissedFullScreenContent = {
            Log.d("TAG", "onAdDismissedFullScreenContent")
        },
        onAdFailedToShowFullScreenContent = {
            Log.d("TAG", "onAdFailedToShowFullScreenContent")

        }
    )
}


/**Loading Admob Native Advertisement To the [ViewGroup]
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


/**
 * Loading Adivery Native Advertisement To the [ViewGroup]
 * @param [onAdLoaded] a lambda function for updating upstreams to react when ever advertisement loaded
 * @param [adiveryNativeLayoutId] of type [LayoutRes] the id of created layout in (project / library ) res folder */
fun ViewGroup.loadAdiveryNativeAdvertisementView(
    onAdLoaded: () -> Unit,
    @LayoutRes adiveryNativeLayoutId: Int
) {
    this.addView(
        AdvertisementCore.requestNativeAds(context, adiveryNativeLayoutId) {
            findViewById<TextView>(R.id.adivery_headline).isSelected = true
            onAdLoaded()
        }
    )

    this.setOnClickListener {
        this.findViewById<AppCompatButton>(R.id.adivery_call_to_action)
            .performClick()
    }
}


/**
 * Loading Adivery Native Advertisement To the [AdiveryNativeAdView]
 * @param [onAdLoaded] a lambda function for updating upstreams to react when ever advertisement loaded
 * @param [adiveryNativeLayoutId] of type [LayoutRes] the id of created layout in (project / library ) res folder */

fun loadAdiveryNativeAdvertisementView(
    context: Context,
    onAdLoaded: (AdiveryNativeAdView) -> Unit,
    @LayoutRes adiveryNativeLayoutId: Int
) {
    var adView: AdiveryNativeAdView? = null

    // Inflate a layout and add it to the parent ViewGroup.
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
            as LayoutInflater
    val adiveryAdView = inflater.inflate(adiveryNativeLayoutId, null, true) as ViewGroup

    adView = AdvertisementCore.requestNativeAds(context, adiveryNativeLayoutId) {
        trying {
            adiveryAdView.findViewById<TextView>(R.id.adivery_headline).isSelected = true

            val adiveryButton =
                adiveryAdView.findViewById<AppCompatButton>(R.id.adivery_call_to_action)

            adiveryButton.setOnClickListener {
                adiveryButton.performClick()
            }

            adView?.let { onAdLoaded(it) }
        }
    }
}





