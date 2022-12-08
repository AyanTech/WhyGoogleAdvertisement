package ir.tafreshiali.whyoogle_ads

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import ir.ayantech.advertisement.core.AdvertisementCore
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementInput
import ir.tafreshiali.whyoogle_ads.ayan_ads.domain.AyanCustomAdvertisementModel

/**
 * Ayan Advertisement Impl
 * for handling application general advertisement (adivery or admob)
 * @param context of type [Application]
 * @param admobAdvertisement of type [AdmobAdvertisement] for handling admob advertisement
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
     * @param admobMainInitializationStatus for triggering upstream layers when ever the main admob initialization is fails ( the [AdapterStatus.State] is  [AdapterStatus.State.NOT_READY])
     * @param onInterstitialAdLoaded for triggering upstream layers when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers when ever admob interstitial advertisement is failed to load
     */

    override fun loadAdmobAdvertisement(
        admobInterstitialAdUnit: String,
        admobMainInitializationStatus: (Boolean) -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit
    ) {

        initializeAdmobAdvertisement(
            admobInterstitialAdUnit = admobInterstitialAdUnit,
            admobMainInitializationStatus = admobMainInitializationStatus,
            onInterstitialAdLoaded = onInterstitialAdLoaded,
            onInterstitialFailed = onInterstitialFailed
        )

    }


    /** this function used for loading adivery advertisement (Maybe This Will Be Removed In The Future)
     * @param adiveryAppKey  of type [String]*/

    override fun loadAdiveryAdvertisement(
        adiveryInterstitialAdUnit: String,
        adiveryAppKey: String
    ) {
        AdvertisementCore.initialize(
            application = context,
            appKey = adiveryAppKey,
            interstitialAdUnitID = "",
            nativeAdUnitID = "",
            bannerAdUnitID = ""
        )

        AdvertisementCore.requestInterstitialAds(
            context = context,
            customAdUnit = adiveryInterstitialAdUnit
        )
    }

    override fun loadAyanCustomNativeAdvertisement(
        ayanApi: AyanApi,
        input: AyanCustomAdvertisementInput,
        callBack: (AyanCustomAdvertisementModel) -> Unit,
        onChangeStatus: OnChangeStatus?,
        onFailure: OnFailure?
    ) {
        ayanApi.call<AyanCustomAdvertisementModel>(
            endPoint = AdvertisementEndpoint.AppConfigAdvertisementContainerInfo,
            input = input
        ) {
            useCommonFailureCallback = false
            useCommonChangeStatusCallback = false
            success { it?.let(callBack) }
            onChangeStatus?.let { changeStatusCallback(it) }
            onFailure?.let { failure(it) }
        }
    }

    /** Here we have two main steps :
     * step 1 - we check if the admob [ClassLoader] is not null . if it is null or not exist we load the adivery advertisement and skip the admob initialization . if admob [ClassLoader] is exist we continue admob initialization (step 2).
     * step 2 - initialize admob and based on the response ([AdapterStatus.State]) we decide to load witch advertisement (adivery or admob)
     * @param admobMainInitializationStatus for triggering upstream layers when ever the main admob initialization is fails ( the [AdapterStatus.State] is  [AdapterStatus.State.NOT_READY])
     * @param onInterstitialAdLoaded for triggering upstream layers when ever admob interstitial advertisement loaded successfully
     * @param onInterstitialFailed for triggering upstream layers when ever admob interstitial advertisement is failed to load*/

    private fun initializeAdmobAdvertisement(
        admobInterstitialAdUnit: String,
        admobMainInitializationStatus: (Boolean) -> Unit,
        onInterstitialAdLoaded: (interstitialAd: InterstitialAd) -> Unit,
        onInterstitialFailed: () -> Unit
    ) {
        if (MobileAds::class.java.classLoader != null) {
            Log.d("Admob", "Admob Is Exist In The Project")

            admobAdvertisement.initInitializeAdmob(
                context = context,
                admobInitializeStatus = { admobStatus ->

                    if (admobStatus == AdapterStatus.State.READY) {

                        /** If the admob main initialization successful
                         * we load admob interstitial advertisement */

                        admobAdvertisement.loadInterstitial(
                            admobInitializeAdvertisementId = admobInterstitialAdUnit,
                            context = context,
                            onInterstitialAdLoaded = onInterstitialAdLoaded,
                            onInterstitialFailed = onInterstitialFailed
                        )
                    }
                    admobMainInitializationStatus(admobStatus == AdapterStatus.State.READY)
                })
        } else {
            Log.d("Admob", "Admob Dose Not Exist In The Project")
            admobMainInitializationStatus(false)
        }
    }
}