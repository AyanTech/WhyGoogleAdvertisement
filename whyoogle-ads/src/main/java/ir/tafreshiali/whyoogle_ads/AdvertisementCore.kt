package ir.tafreshiali.whyoogle_ads


import android.app.Application
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisementImpl

object AdvertisementCore {

    var admobInterstitialAdvertisement: InterstitialAd? = null

    var admobNativeAdvertisement: NativeAd? = null

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

    fun updateApplicationAdmobNativeAdvertisement(admobNativeAd: NativeAd? = null) {
        admobNativeAdvertisement = admobNativeAd

    }
}
