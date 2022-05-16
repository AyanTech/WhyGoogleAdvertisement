package ir.tafreshiali.whyoogle_ads.admob

import android.app.Application
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

interface AdmobAdvertisement {

    /**
     * Admob Initialization
     * When ever server set the advertisement source to admob we do the following step
     * call [initInitializeAdmob] the based on initializationState of type [AdapterStatus.State] we decide to load admob ads or load adivery ads
     * * @param context of type [Application]
     * @param admobInitializeStatus a lambda function that returns the [AdapterStatus.State] for triggering upstream layers to decide what to do*/

    fun initInitializeAdmob(
        context: Application,
        admobInitializeStatus: (AdapterStatus.State) -> Unit
    )

    /**
     *  function for loading the admob interstitial advertisement
     * @param context of type [Application]
     * @param admobInitializeAdvertisementId the Optional admob advertisement id for loading interstitial ads
     * Important Note about [admobInitializeAdvertisementId] = the recommended way is to not pass the [admobInitializeAdvertisementId] directly instead when ever the advertisement request is succeed save information to the [appDataStoreManager]
     * @param onInterstitialAdLoaded for triggering upstream layers to be react when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers to be react when ever admob interstitial advertisement fail to load*/


     fun loadInterstitial(
        context: Application,
        admobInitializeAdvertisementId: String,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit
    )


    /**
     * Admob Interstitial Callbacks
     * this callbacks needs to be added (when ever the new interstitial admob advertisement loaded / when ever [loadInterstitial] is successful)
     * @param interstitialAd of type [InterstitialAd] this parameter should  be quantified
     * each time the interstitial admob ads are loaded
     * @param onAdDismissedFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement Ui Is Dismissed
     * @param onAdFailedToShowFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement failed to show
     * @param onAdShowedFullScreenContent for triggering upstream layers to be react when ever admob interstitial advertisement is successfully shown to the user
     * */

    fun addInterstitialCallbacks(
        interstitialAd: InterstitialAd,
        onAdDismissedFullScreenContent: () -> Unit,
        onAdFailedToShowFullScreenContent: () -> Unit,
        onAdShowedFullScreenContent: () -> Unit,
    )

    /**
     * Admob Native Advertisement
     * before showing each native advertisement you should request for it then
     * use the created [NativeAd] object to handle the native view
     * @param context of type [Application]
     * @param admobNativeAdvertisementId the required admob advertisement id for loading native ads
     * @param onNativeAdFailed for triggering upstream layers to react when ever admob native request fails
     * @param onNativeAdLoaded for trigger upstream layers to react when ever admob native request is succeed
     * */

     fun loadNativeAdLoader(
        context: Application,
        admobNativeAdvertisementId: String,
        onNativeAdLoaded: (ad: NativeAd) -> Unit,
        onNativeAdFailed: () -> Unit
    )
}