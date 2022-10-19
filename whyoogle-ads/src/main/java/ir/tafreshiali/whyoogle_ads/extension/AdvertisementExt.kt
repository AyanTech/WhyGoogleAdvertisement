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
import androidx.appcompat.widget.AppCompatButton
import com.adivery.sdk.AdiveryNativeAdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import ir.ayantech.advertisement.core.AdvertisementCore
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
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
 * @param callback a lambda function for doing some operation on the advertisement activation state
 * @param handleAdiveryNativeAdvertisement
 * @param showAdmobNativeAdvertisement*/
fun AppConfigAdvertisementOutput.checkAdvertisementStatus(
    application: Application,
    ayanAdvertisement: AyanAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.ayanAdvertisement,
    callback: (Boolean) -> Unit,
    handleAdiveryNativeAdvertisement: () -> Unit,
    showAdmobNativeAdvertisement: () -> Unit
) {
    if (this.Active) {
        ApplicationAdvertisementType.appAdvertisementType =
            this.Sources.firstOrNull { it.Key == ApplicationAdvertisementType.APPLICATION_ADVERTISEMENT_SOURCE }?.Value
                ?: return

        when (ApplicationAdvertisementType.appAdvertisementType) {

            AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY -> {
                this.initializeAdmobAdvertisement(
                    ayanAdvertisement = ayanAdvertisement,
                    handleAdiveryNativeAdvertisement = handleAdiveryNativeAdvertisement,
                    handleAdmobInterstitialAdvertisement = { admobInterstitialAd ->
                        ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobInterstitialAdvertisement(
                            admobInterstitialAd = admobInterstitialAd
                        )


                        ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobInterstitialAdvertisement?.addAdmobInterstitialCallback(
                            ayanAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.ayanAdvertisement,
                            application = application,
                            admobAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobAdvertisement
                        )
                    },
                    handleAdmobNativeAdvertisement = { admobNativeAd ->
                        ir.tafreshiali.whyoogle_ads.AdvertisementCore.updateApplicationAdmobNativeAdvertisement(
                            admobNativeAd = admobNativeAd
                        )
                    },
                    showAdmobNativeAdvertisement = showAdmobNativeAdvertisement
                )
            }

            AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
                this.initializeAdiveryAdvertisement(ayanAdvertisement)
                handleAdiveryNativeAdvertisement()
            }
        }
    }
    callback.invoke(this.Active)
}


/**
 * initializeAdiveryAdvertisement Based on [AppConfigAdvertisementOutput]
 * First we check if the source of the advertisement equals to [AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY] or not
 * and then follow up the rest of the adivery initializing process
 * @param ayanAdvertisement of type [AyanAdvertisement]
 * */
fun AppConfigAdvertisementOutput.initializeAdiveryAdvertisement(ayanAdvertisement: AyanAdvertisement) {

    if (ApplicationAdvertisementType.appAdvertisementType == AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY) {
        ApplicationAdvertisementType.appNativeAdvertisementType =
            AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
        ApplicationAdvertisementType.appInterstitialAdvertisementType =
            AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
    }

    ayanAdvertisement.saveAdiveryAdvertisementKeys(
        appKey = this.Sources.firstOrNull { it.Key == AdiveryAdvertisementKey.APP_AD_KEY }?.Value
            ?: return,
        interstitialAdUnitID = this.Sources.firstOrNull { it.Key == AdiveryAdvertisementKey.AD_INTERSTITIAL_KEY }?.Value
            ?: return,
        nativeAdUnitID = this.Sources.firstOrNull { it.Key == AdiveryAdvertisementKey.AD_NATIVE_KEY }?.Value
            ?: return,
        bannerAdUnitID = this.Sources.firstOrNull { it.Key == AdiveryAdvertisementKey.AD_BANNER_KEY }?.Value
            ?: return
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
        nativeAdvertisementId = this.Sources.firstOrNull { it.Key == AdmobAdvertisementKey.AD_NATIVE_KEY }?.Value
            ?: return,
        interstitialAdvertisementId = this.Sources.firstOrNull { it.Key == AdmobAdvertisementKey.AD_INTERSTITIAL_KEY }?.Value
            ?: return
    )

    ayanAdvertisement.loadAdmobAdvertisement(
        onMainAdmobInitializationFailed = {
            ApplicationAdvertisementType.appAdvertisementType =
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
            ApplicationAdvertisementType.appInterstitialAdvertisementType =
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
            ApplicationAdvertisementType.appNativeAdvertisementType =
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
            handleAdiveryNativeAdvertisement()
        },
        onInterstitialAdLoaded = {

            handleAdmobInterstitialAdvertisement(it)

            ApplicationAdvertisementType.appInterstitialAdvertisementType =
                AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY
        },
        onInterstitialFailed = {
            handleAdmobInterstitialAdvertisement(null)
            ApplicationAdvertisementType.appInterstitialAdvertisementType =
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
        },
        onNativeAdFailed = {
            handleAdmobNativeAdvertisement(null)
            ApplicationAdvertisementType.appNativeAdvertisementType =
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
            handleAdiveryNativeAdvertisement()
        },
        onNativeAdLoaded = {
            handleAdmobNativeAdvertisement(it)
            ApplicationAdvertisementType.appNativeAdvertisementType =
                AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY
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
                        AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY
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
    onAdLoaded: (AdiveryNativeAdView) -> Unit,
    @LayoutRes adiveryNativeLayoutId: Int
) {
    var adView: AdiveryNativeAdView? = null

    adView = AdvertisementCore.requestNativeAds(context, adiveryNativeLayoutId) {
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
 * @param [applicationAdvertisementType] [applicationNativeAdvertisementType] for handling different type of native advertisement
 * @param [loadAdiveryNativeView] [loadAdmobNativeView] a lambda functions for updating upstreams to react when ever interstitial advertisement requested */

fun handleApplicationNativeAdvertisement(
    applicationAdvertisementType: String = ApplicationAdvertisementType.appAdvertisementType,
    applicationNativeAdvertisementType: String = ApplicationAdvertisementType.appNativeAdvertisementType,
    loadAdiveryNativeView: () -> Unit,
    loadAdmobNativeView: () -> Unit
) {

    when (applicationAdvertisementType) {
        AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY -> {
            when (applicationNativeAdvertisementType) {
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
                    loadAdiveryNativeView()
                }

                AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY -> {
                    loadAdmobNativeView()
                }
            }
        }

        AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
            when (applicationNativeAdvertisementType) {
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
                    loadAdiveryNativeView()
                }
            }
        }
    }
}


/** handling application interstitial advertisement
 * @param activity we need activity for loading admob advertisement
 * @param [ayanAdvertisement] [admobAdvertisement] for loading admob advertisement
 * @param [loadAdmobInterstitialAdvertisement] [loadAdiveryInterstitialAdvertisement] a lambda functions for updating upstreams to react when ever interstitial advertisement requested  */

fun showApplicationInterstitialAdvertisement(
    activity: Activity,
    ayanAdvertisement: AyanAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.ayanAdvertisement,
    admobAdvertisement: AdmobAdvertisement = ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobAdvertisement,
    loadAdmobInterstitialAdvertisement: () -> Unit = {},
    loadAdiveryInterstitialAdvertisement: () -> Unit = {}
) {
    when (ApplicationAdvertisementType.appAdvertisementType) {
        AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY -> {
            when (ApplicationAdvertisementType.appInterstitialAdvertisementType) {
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
                    AdvertisementCore.showInterstitialAds()
                    loadAdiveryInterstitialAdvertisement()
                }

                AdmobAdvertisementKey.ADMOB_ADVERTISEMENT_KEY -> {

                    ir.tafreshiali.whyoogle_ads.AdvertisementCore.admobInterstitialAdvertisement?.let { admobInterstitialAd ->
                        admobInterstitialAd.addAdmobInterstitialCallback(
                            application = activity.application,
                            ayanAdvertisement = ayanAdvertisement,
                            admobAdvertisement = admobAdvertisement
                        )

                        admobInterstitialAd.show(activity)

                        loadAdmobInterstitialAdvertisement()
                    }
                }
            }
        }

        AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
            when (ApplicationAdvertisementType.appInterstitialAdvertisementType) {
                AdiveryAdvertisementKey.ADIVERY_ADVERTISEMENT_KEY -> {
                    AdvertisementCore.showInterstitialAds()
                    loadAdiveryInterstitialAdvertisement()
                }
            }
        }
    }
}


