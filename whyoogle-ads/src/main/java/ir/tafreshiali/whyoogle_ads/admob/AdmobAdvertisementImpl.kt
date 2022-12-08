package ir.tafreshiali.whyoogle_ads.admob

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

/** Admob Advertisement Impl
 * for handling application admob advertisement*/
class AdmobAdvertisementImpl : AdmobAdvertisement {

    /**
     * Admob Initialization
     * When ever server set the advertisement source to admob we do the following step
     * call [initInitializeAdmob] the based on initializationState of type [AdapterStatus.State] we decide to load admob ads or load adivery ads
     * * @param context of type [Application]
     * @param admobInitializeStatus a lambda function that returns the [AdapterStatus.State] for triggering upstream layers to decide what to do*/
    override fun initInitializeAdmob(
        context: Application,
        admobInitializeStatus: (AdapterStatus.State) -> Unit
    ) {
        MobileAds.initialize(context) { admobStatus ->
            admobStatus.adapterStatusMap.values.forEachIndexed { _, adapterStatus ->
                admobInitializeStatus(adapterStatus.initializationState)

                if (adapterStatus.initializationState == AdapterStatus.State.READY)
                    Log.d(
                        "ADMOB_STATUS",
                        "initAdmob adapterStatus Description Is =${adapterStatus.description} and adapterStatus initializationState is =${adapterStatus.initializationState} it is Ready"
                    )

                if (adapterStatus.initializationState == AdapterStatus.State.NOT_READY)
                    Log.d(
                        "ADMOB_STATUS",
                        "initAdmob adapterStatus Description Is =${adapterStatus.description} and adapterStatus initializationState is =${adapterStatus.initializationState} it is Not Ready"
                    )
            }
        }
    }

    /**
     * a function for loading the admob interstitial advertisement
     * @param context of type [Application]
     * @param admobInitializeAdvertisementId the Optional admob advertisement id for loading interstitial ads
     * Important Note about [admobInitializeAdvertisementId] = the recommended way is to not pass the [admobInitializeAdvertisementId] directly instead when ever the advertisement request is succeed save information to the [android.content.SharedPreferences]
     * @param onInterstitialAdLoaded for triggering upstream layers to be react when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers to be react when ever admob interstitial advertisement fail to load*/

    override fun loadInterstitial(
        context: Application,
        admobInitializeAdvertisementId: String,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit
    ) {
        InterstitialAd.load(
            context,
            admobInitializeAdvertisementId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    onInterstitialAdLoaded(interstitialAd)
                    Log.d("Admob", "onAdLoaded: Ad was loaded.")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    onInterstitialFailed()
                    Log.d("Admob", "onAdFailedToLoad: ${loadAdError.message}")
                }
            }
        )
    }

    /**
     * Admob Interstitial Callbacks
     * this callbacks needs to be added (when ever the new interstitial admob advertisement loaded / when ever [loadInterstitial] is successful)
     * @param interstitialAd of type [InterstitialAd] this parameter should  be quantified
     * each time the interstitial admob ads are loaded
     * @param onAdDismissedFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement Ui Is Dismissed
     * @param onAdFailedToShowFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement failed to show
     * @param onAdShowedFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement is successfully shown to the user
     * */

    override fun addInterstitialCallbacks(
        interstitialAd: InterstitialAd,
        onAdDismissedFullScreenContent: () -> Unit,
        onAdFailedToShowFullScreenContent: () -> Unit,
        onAdShowedFullScreenContent: () -> Unit,
    ) {
        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdDismissedFullScreenContent()
                Log.d("Admob", "onAdDismissedFullScreenContent: Ad was dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                onAdFailedToShowFullScreenContent()
                Log.d(
                    "Admob",
                    "onAdFailedToShowFullScreenContent: Ad failed to show the messages is ${p0.message} and the cause is ${p0.cause}"
                )
            }

            override fun onAdShowedFullScreenContent() {
                onAdShowedFullScreenContent()
                Log.d("Admob", "onAdShowedFullScreenContent: Ad showed fullscreen content.")
            }
        }
    }

    /**
     * Admob Native Advertisement
     * before showing each native advertisement you should request for it then
     * use the created [NativeAd] object to handle the native view
     * @param context of type [Application]
     * @param admobNativeAdvertisementId the required admob advertisement id for loading native ads
     * @param onNativeAdFailed for triggering upstream layers to react when ever admob native request fails
     * @param onNativeAdLoaded for trigger upstream layers to react when ever admob native request is succeed
     * */

    override fun loadNativeAdLoader(
        context: Application,
        admobNativeAdvertisementId: String,
        onNativeAdLoaded: (ad: NativeAd) -> Unit,
        onNativeAdFailed: () -> Unit
    ) {

        val adLoader = AdLoader.Builder(
            context,
            admobNativeAdvertisementId
        )
            .forNativeAd { ad: NativeAd ->

                onNativeAdLoaded(ad)
                Log.d("Admob", "Native Admob Advertisement is Loaded")
                Log.d(
                    "Admob",
                    "Native Admob Advertisement is Loaded and the title is ${ad.headline}"
                )
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                    onNativeAdFailed()
                    Log.d("Admob", "Native Admob Advertisement is Failed")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}