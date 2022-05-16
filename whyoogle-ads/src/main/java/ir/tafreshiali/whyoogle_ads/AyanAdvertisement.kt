package ir.tafreshiali.whyoogle_ads

import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import ir.tafreshiali.whyoogle_ads.adivery.AdiveryAdvertisementProperties
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisementProperties

interface AyanAdvertisement {


    /**
     *  function for loading admob advertisement
     * In Admob Advertisement we should load adivery
     * too for situations that admob ( initialization / native / interstitial  )
     * fails and replace them with adivery Equivalent Advertisements
     * @param adiveryAdvertisementProperties
     * @param admobAdvertisementProperties
     * @param onMainAdmobInitializationFailed for triggering upstream layers when ever the main admob initialization is fails ( the [AdapterStatus.State] is  [AdapterStatus.State.NOT_READY])
     * @param onInterstitialAdLoaded for triggering upstream layers when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers when ever admob interstitial advertisement is failed to load
     * @param onNativeAdLoaded for triggering upstream layers when ever admob native advertisement is loaded successfully
     * @param onNativeAdFailed for triggering upstream layers when ever admob native advertisement is failed to load*/


     fun loadAdmobAdvertisement(
        adiveryAdvertisementProperties: AdiveryAdvertisementProperties? = null,
        admobAdvertisementProperties: AdmobAdvertisementProperties? = null,
        onMainAdmobInitializationFailed: () -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit,
        onNativeAdLoaded: (ad: NativeAd) -> Unit,
        onNativeAdFailed: () -> Unit
    )


    /**  function for loading adivery advertisement (Maybe This Will Be Removed In The Future)
     * @param adiveryAdvertisementProperties nullable of type [AdiveryAdvertisementProperties]
     * Important Note = recommended way for loading app advertisement is to save advertisement properties and the use them
     * */

     fun loadAdiveryAdvertisement(adiveryAdvertisementProperties: AdiveryAdvertisementProperties)


    /**  function for saving admob advertisement properties that server sends to the client
     * @param [nativeAdvertisementId] [interstitialAdvertisementId] */

     fun saveAdmobAdvertisementKeys(
        nativeAdvertisementId: String,
        interstitialAdvertisementId: String,
    )

    /**  function for saving adivery advertisement properties that server sends to the cilent
     * @param [appKey] [interstitialAdUnitID] [nativeAdUnitID] [bannerAdUnitID]*/

     fun saveAdiveryAdvertisementKeys(
        appKey: String,
        interstitialAdUnitID: String,
        nativeAdUnitID: String,
        bannerAdUnitID: String
    )

    /**  function for reading [AdiveryAdvertisementProperties]
     * @return the saved [AdiveryAdvertisementProperties]*/

     fun readAdiveryAdvertisementProperties(): AdiveryAdvertisementProperties

    /**  function for reading [AdmobAdvertisementProperties]
     * @return the saved [AdmobAdvertisementProperties]*/

     fun readAdmobAdvertisementProperties(): AdmobAdvertisementProperties
}