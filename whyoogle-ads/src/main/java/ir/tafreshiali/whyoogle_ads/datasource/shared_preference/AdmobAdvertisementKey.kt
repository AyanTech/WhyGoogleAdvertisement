package ir.tafreshiali.whyoogle_ads.datasource.shared_preference

import android.annotation.SuppressLint
import android.content.Context
import ir.ayantech.whygoogle.helper.PreferencesManager

@SuppressLint("StaticFieldLeak")
object AdmobAdvertisementKey {

    private const val APP_AD_KEY = "admobappkey"
    private const val AD_INTERSTITIAL_KEY = "admobInterstitialAdUnitID"
    private const val AD_BANNER_KEY = "admobBannerAdUnitID"
    private const val AD_NATIVE_KEY = "admobNativeAdUnitID"

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }


    var interstitialAdvertisementKey: String
        get() = PreferencesManager.getInstance(context).read(fieldName = AD_INTERSTITIAL_KEY)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = AD_INTERSTITIAL_KEY, value = value)


    var nativeAdvertisementKey: String
        get() = PreferencesManager.getInstance(context).read(fieldName = AD_NATIVE_KEY)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = AD_NATIVE_KEY, value = value)

}