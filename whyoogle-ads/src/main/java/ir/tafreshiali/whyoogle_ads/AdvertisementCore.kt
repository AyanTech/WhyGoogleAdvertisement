package ir.tafreshiali.whyoogle_ads


import android.app.Application
import android.content.Context
import android.view.ViewGroup
//import com.adivery.sdk.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisementImpl

object AdvertisementCore {

    var interstitialAdUnitID: String = ""
    var bannerAdUnitID: String = ""
    var nativeAdUnitID: String = ""

    var admobInterstitialAdvertisement: InterstitialAd? = null

    lateinit var admobAdvertisement: AdmobAdvertisement

    lateinit var ayanAdvertisement: AyanAdvertisement

    fun init(application: Application) {
        admobAdvertisement = AdmobAdvertisementImpl()
        ayanAdvertisement = AyanAdvertisementImpl(
            context = application,
            admobAdvertisement = admobAdvertisement
        )
    }

    fun updateApplicationAdmobInterstitialAdvertisement(admobInterstitialAd: InterstitialAd?) {
        admobInterstitialAdvertisement = admobInterstitialAd
    }

    fun initialize(
        application: Application,
        appKey: String,
        interstitialAdUnitID: String,
        bannerAdUnitID: String,
        nativeAdUnitID: String,
    ) {
        this.interstitialAdUnitID = interstitialAdUnitID
        this.bannerAdUnitID = bannerAdUnitID
        this.nativeAdUnitID = nativeAdUnitID
//        Adivery.configure(application, appKey)
    }

    fun requestInterstitialAds(
        context: Context,
        customAdUnit: String? = null,
        onAdLoaded: StringCallback? = null,
        onAdClicked: StringCallback? = null,
        onAdShown: StringCallback? = null,
        onAdClosed: StringCallback? = null,
        onAdError: TwoStringCallback? = null
    ) {
        //one time call is enough. adivery will prepare next ad after showing current ad automatically
//        Adivery.prepareInterstitialAd(context, customAdUnit ?: interstitialAdUnitID)
//
//        Adivery.addGlobalListener(
//            simplifiedInterstitialAdListener(
//                onAdLoaded,
//                onAdClicked,
//                onAdShown,
//                onAdClosed,
//                onAdError
//            )
//        )
    }

//    private fun simplifiedInterstitialAdListener(
//        onAdLoaded: StringCallback?,
//        onAdClicked: StringCallback?,
//        onAdShown: StringCallback?,
//        onAdClosed: StringCallback?,
//        logCallback: TwoStringCallback?,
//    ) = object : AdiveryListener() {
//        override fun onInterstitialAdLoaded(placementId: String) {
//            onAdLoaded?.invoke(placementId)
//        }
//
//        override fun onInterstitialAdClicked(placementId: String) {
//            onAdClicked?.invoke(placementId)
//        }
//
//        override fun onInterstitialAdClosed(placement: String) {
//            onAdClosed?.invoke(placement)
//        }
//
//        override fun onInterstitialAdShown(placementId: String) {
//            onAdShown?.invoke(placementId)
//        }
//
//        override fun log(placementId: String, message: String) {
//            logCallback?.invoke(placementId, message)
//        }
//    }

    fun showInterstitialAds(
        customAdUnit: String? = null,
        onAdLoaded: StringCallback? = null,
        onAdClicked: StringCallback? = null,
        onAdShown: StringCallback? = null,
        onAdClosed: StringCallback? = null,
        logCallback: TwoStringCallback? = null
    ) {
//        if (Adivery.isLoaded(customAdUnit ?: interstitialAdUnitID)
//        ) {
//            Adivery.showAd(customAdUnit ?: interstitialAdUnitID)
//        }

//        Adivery.addGlobalListener(
//            simplifiedInterstitialAdListener(
//                onAdLoaded,
//                onAdClicked,
//                onAdShown,
//                onAdClosed,
//                logCallback
//            )
//        )
    }

//    fun requestBannerAds(
//        context: Context,
//        viewGroup: ViewGroup,
//        bannerSize: BannerSize? = null,
//        customAdUnit: String? = null,
//        onAdLoaded: SimpleCallback? = null,
//        onAdClicked: SimpleCallback? = null,
//        onAdShown: SimpleCallback? = null,
//        onError: StringCallback? = null
//    ) {
//        viewGroup.removeAllViews()
//        val adView = AdiveryBannerAdView(context)
//        adView.setPlacementId(customAdUnit ?: bannerAdUnitID)
//        adView.setBannerSize(bannerSize ?: BannerSize.BANNER)
//        adView.loadAd()
//        viewGroup.addView(adView)
//        adView.setBannerAdListener(
//            simplifiedNativeAndBannerAdListener(
//                onAdClicked,
//                onAdShown,
//                onError,
//                onAdLoaded
//            )
//        )
//    }

//    private fun simplifiedNativeAndBannerAdListener(
//        onAdClicked: SimpleCallback?,
//        onAdShown: SimpleCallback?,
//        onAdError: StringCallback?,
//        onAdLoaded: SimpleCallback?,
//    ) = object : AdiveryAdListener() {
//        override fun onAdLoaded() {
//            onAdLoaded?.invoke()
//        }
//
//        override fun onAdClicked() {
//            onAdClicked?.invoke()
//        }
//
//        override fun onAdShown() {
//            onAdShown?.invoke()
//        }
//
//        override fun onError(reason: String) {
//            onAdError?.invoke(reason)
//        }
//    }

//    fun requestNativeAds(
//        context: Context,
//        layoutId: Int,
//        customAdUnit: String? = null,
//        onAdClicked: SimpleCallback? = null,
//        onAdShown: SimpleCallback? = null,
//        onError: StringCallback? = null,
//        onAdLoaded: SimpleCallback? = null,
//    ): AdiveryNativeAdView {
//        val adiveryNativeAdView = AdiveryNativeAdView(context)
//        trying {
//            adiveryNativeAdView.setNativeAdLayout(layoutId)
//            adiveryNativeAdView.setPlacementId(
//                customAdUnit ?: nativeAdUnitID
//            )
//            adiveryNativeAdView.setListener(
//                simplifiedNativeAndBannerAdListener(
//                    onAdClicked,
//                    onAdShown,
//                    onError,
//                    onAdLoaded
//                )
//            )
//            adiveryNativeAdView.loadAd()
//        }
//        return adiveryNativeAdView
//    }
}
