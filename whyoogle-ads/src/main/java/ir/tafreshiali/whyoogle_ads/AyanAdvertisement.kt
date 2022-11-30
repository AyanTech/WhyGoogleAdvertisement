package ir.tafreshiali.whyoogle_ads

import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd

interface AyanAdvertisement {


    /**
     *  function for loading admob advertisement
     * In Admob Advertisement we should load adivery
     * too for situations that admob ( initialization / native / interstitial  )
     * fails and replace them with adivery Equivalent Advertisements
     * @param onMainAdmobInitializationFailed for triggering upstream layers when ever the main admob initialization is fails ( the [AdapterStatus.State] is  [AdapterStatus.State.NOT_READY])
     * @param onInterstitialAdLoaded for triggering upstream layers when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers when ever admob interstitial advertisement is failed to load*/


    fun loadAdmobAdvertisement(
        admobInterstitialAdUnit: String,
        onMainAdmobInitializationFailed: () -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit
    )


    /**  function for loading adivery advertisement (Maybe This Will Be Removed In The Future)
     * @param adiveryAppKey of type [String] for initializing adivery
     * @param adiveryInterstitialAdUnit of type [String] for initializing adivery interstitial advertisement
     * */

    fun loadAdiveryAdvertisement(adiveryInterstitialAdUnit: String,adiveryAppKey: String)

}