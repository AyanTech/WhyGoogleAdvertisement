package ir.tafreshiali.whyoogle_ads.datasource.shared_preference

import android.annotation.SuppressLint
import android.content.Context
import ir.ayantech.whygoogle.helper.PreferencesManager

@SuppressLint("StaticFieldLeak")
object AdiveryAdvertisementKey {

    private const val APP_AD_KEY = "adiveryappkey"
    private const val AD_INTERSTITIAL_KEY = "adiveryInterstitialAdUnitID"
    private const val AD_BANNER_KEY = "adiveryBannerAdUnitID"
    private const val AD_NATIVE_KEY = "adiveryNativeAdUnitID"

    private lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }

    var appAdvertisementKey: String
        get() = PreferencesManager.getInstance(context).read(fieldName = APP_AD_KEY)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = APP_AD_KEY, value = value)

    var interstitialAdvertisementKey: String
        get() = PreferencesManager.getInstance(context).read(fieldName = AD_INTERSTITIAL_KEY)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = AD_INTERSTITIAL_KEY, value = value)

    var bannerAdvertisementKey: String
            get() = PreferencesManager.getInstance(context).read(fieldName = AD_BANNER_KEY)
            set(value) = PreferencesManager.getInstance(context)
                .save(fieldName = AD_BANNER_KEY, value = value)

    
    var nativeAdvertisementKey: String
            get() = PreferencesManager.getInstance(context).read(fieldName = AD_NATIVE_KEY)
            set(value) = PreferencesManager.getInstance(context)
                .save(fieldName = AD_NATIVE_KEY, value = value)



}