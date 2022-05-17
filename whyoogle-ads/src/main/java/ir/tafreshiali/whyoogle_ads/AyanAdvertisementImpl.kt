package ir.tafreshiali.whyoogle_ads

import android.app.Application
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import ir.ayantech.advertisement.core.AdvertisementCore
import ir.tafreshiali.whyoogle_ads.adivery.AdiveryAdvertisementProperties
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisementProperties
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdiveryAdvertisementKey
import ir.tafreshiali.whyoogle_ads.datasource.shared_preference.AdmobAdvertisementKey

/**
 * Ayan Advertisement Impl
 * for handling application general advertisement (adivery or admob)
 * @param context of type [Application]
 * @param admobAdvertisement of type [AdmobAdvertisement] for handling admob advertisement
 * @param [appDataStoreManager] of type [AppDataStore]
 * @param externalScope of type [CoroutineScope] for reading data form data store
 */
class AyanAdvertisementImpl(
    private val context: Application,
    private val admobAdvertisement: AdmobAdvertisement
) : AyanAdvertisement {

    /**
     * function for loading admob advertisement
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

    override fun loadAdmobAdvertisement(
        adiveryAdvertisementProperties: AdiveryAdvertisementProperties?,
        admobAdvertisementProperties: AdmobAdvertisementProperties?,
        onMainAdmobInitializationFailed: () -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit,
        onNativeAdLoaded: (ad: NativeAd) -> Unit,
        onNativeAdFailed: () -> Unit
    ) {

        initializeAdiveryAndRequestForInterstitialAdvertisement(
            adiveryAdvertisementProperties = adiveryAdvertisementProperties
                ?: readAdiveryAdvertisementProperties()
        )

        initializeAdmobAdvertisement(
            admobAdvertisementProperties = admobAdvertisementProperties
                ?: readAdmobAdvertisementProperties(),
            adiveryAdvertisementProperties = adiveryAdvertisementProperties
                ?: readAdiveryAdvertisementProperties(),
                onMainAdmobInitializationFailed = onMainAdmobInitializationFailed,
            onInterstitialAdLoaded = onInterstitialAdLoaded,
            onInterstitialFailed = onInterstitialFailed,
            onNativeAdLoaded = onNativeAdLoaded,
            onNativeAdFailed = onNativeAdFailed
        )

    }


    /** [] function for loading adivery advertisement (Maybe This Will Be Removed In The Future)
     * @param adiveryAdvertisementProperties  of type [AdiveryAdvertisementProperties]
     * Important Note = recommended way for loading app advertisement is to save advertisement properties and the use them
     * */

    override fun loadAdiveryAdvertisement(adiveryAdvertisementProperties: AdiveryAdvertisementProperties) {
        initializeAdiveryAndRequestForInterstitialAdvertisement(
            adiveryAdvertisementProperties = adiveryAdvertisementProperties
        )
    }

    /** function for saving admob advertisement properties that server sends to the client
     * @param [nativeAdvertisementId] [interstitialAdvertisementId] */

    override fun saveAdmobAdvertisementKeys(
        nativeAdvertisementId: String,
        interstitialAdvertisementId: String
    ) {
        AdmobAdvertisementKey.interstitialAdvertisementKey = interstitialAdvertisementId
        AdmobAdvertisementKey.nativeAdvertisementKey = nativeAdvertisementId
    }

    /** function for saving adivery advertisement properties that server sends to the cilent
     * @param [appKey] [interstitialAdUnitID] [nativeAdUnitID] [bannerAdUnitID]*/

    override fun saveAdiveryAdvertisementKeys(
        appKey: String,
        interstitialAdUnitID: String,
        nativeAdUnitID: String,
        bannerAdUnitID: String
    ) {
        AdiveryAdvertisementKey.appAdvertisementKey = appKey
        AdiveryAdvertisementKey.interstitialAdvertisementKey = interstitialAdUnitID
        AdiveryAdvertisementKey.nativeAdvertisementKey = nativeAdUnitID
        AdiveryAdvertisementKey.bannerAdvertisementKey = bannerAdUnitID
    }


    /**  function for reading [AdiveryAdvertisementProperties]
     * @return the saved [AdiveryAdvertisementProperties]*/

    override fun readAdiveryAdvertisementProperties(): AdiveryAdvertisementProperties =
        AdiveryAdvertisementProperties(
            appKey = AdiveryAdvertisementKey.appAdvertisementKey,
            interstitialAdUnitID = AdiveryAdvertisementKey.interstitialAdvertisementKey,
            bannerAdUnitID = AdiveryAdvertisementKey.bannerAdvertisementKey,
            nativeAdUnitID = AdiveryAdvertisementKey.nativeAdvertisementKey,
        )

    /** function for reading [AdmobAdvertisementProperties]
     * @return the saved [AdmobAdvertisementProperties]*/
    override fun readAdmobAdvertisementProperties(): AdmobAdvertisementProperties =
        AdmobAdvertisementProperties(
            nativeAdvertisementId = AdmobAdvertisementKey.nativeAdvertisementKey,
            interstitialAdvertisementId = AdmobAdvertisementKey.interstitialAdvertisementKey
        )


    /**
     * adivery advertisement initialization
     * @param adiveryAdvertisementProperties of type [AdiveryAdvertisementProperties]*/

    private fun initializeAdiveryAndRequestForInterstitialAdvertisement(
        adiveryAdvertisementProperties: AdiveryAdvertisementProperties,
    ) {
        AdvertisementCore.initialize(
            application = context,
            appKey = adiveryAdvertisementProperties.appKey,
            interstitialAdUnitID = adiveryAdvertisementProperties.interstitialAdUnitID,
            nativeAdUnitID = adiveryAdvertisementProperties.nativeAdUnitID,
            bannerAdUnitID = adiveryAdvertisementProperties.bannerAdUnitID
        )

        AdvertisementCore.requestInterstitialAds(context = context)
    }

    /** Here we initialize admob and based on the response we decide to load witch advertisement (adivery or admob)
     * @param onMainAdmobInitializationFailed for triggering upstream layers when ever the main admob initialization is fails ( the [AdapterStatus.State] is  [AdapterStatus.State.NOT_READY])
     * @param onInterstitialAdLoaded for triggering upstream layers when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers when ever admob interstitial advertisement is failed to load
     * @param onNativeAdLoaded for triggering upstream layers when ever admob native advertisement is loaded successfully
     * @param onNativeAdFailed for triggering upstream layers when ever admob native advertisement is failed to load*/

    private fun initializeAdmobAdvertisement(
        admobAdvertisementProperties: AdmobAdvertisementProperties,
        adiveryAdvertisementProperties: AdiveryAdvertisementProperties,
        onMainAdmobInitializationFailed: () -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit,
        onNativeAdLoaded: (ad: NativeAd) -> Unit,
        onNativeAdFailed: () -> Unit
    ) {
        admobAdvertisement.initInitializeAdmob(
            context = context,
            admobInitializeStatus = { admobStatus ->


                if (admobStatus == AdapterStatus.State.READY) {

                    /** If the admob main initialization successful
                     * we load admob interstitial and native advertisement */

                    admobAdvertisement.loadInterstitial(
                        admobInitializeAdvertisementId = admobAdvertisementProperties.interstitialAdvertisementId,
                        context = context,
                        onInterstitialAdLoaded = onInterstitialAdLoaded,
                        onInterstitialFailed = onInterstitialFailed
                    )

                    admobAdvertisement.loadNativeAdLoader(
                        admobNativeAdvertisementId = admobAdvertisementProperties.nativeAdvertisementId,
                        context = context,
                        onNativeAdLoaded = onNativeAdLoaded,
                        onNativeAdFailed = onNativeAdFailed
                    )

                }

                if (admobStatus == AdapterStatus.State.NOT_READY) {

                    /** If the admob main initialization Fail
                     * we load just Adivery Advertisement */

                    loadAdiveryAdvertisement(adiveryAdvertisementProperties = adiveryAdvertisementProperties)
                    onMainAdmobInitializationFailed()
                }
            })
    }
}