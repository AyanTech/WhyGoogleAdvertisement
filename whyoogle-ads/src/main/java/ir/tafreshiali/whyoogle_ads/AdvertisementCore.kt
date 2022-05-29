package ir.tafreshiali.whyoogle_ads


import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

object AdvertisementCore {

    var admobInterstitialAdvertisement: InterstitialAd? = null
    var admobNativeAdvertisement: NativeAd? = null


    fun updateApplicationAdmobInterstitialAdvertisement(admobInterstitialAd: InterstitialAd?) {
        admobInterstitialAdvertisement = admobInterstitialAd
    }

    fun updateApplicationAdmobNativeAdvertisement(admobNativeAd: NativeAd? = null) {
        admobNativeAdvertisement = admobNativeAd
        
    }
}
