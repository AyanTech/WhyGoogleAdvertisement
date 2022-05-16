package ir.tafreshiali.whyoogle_ads.datasource.shared_preference

import android.annotation.SuppressLint
import android.content.Context
import ir.ayantech.whygoogle.helper.PreferencesManager


@SuppressLint("StaticFieldLeak")
object ApplicationAdvertisementType {

    private const val APPLICATION_ADVERTISEMENT_TYPE = "app_add_type"
    private const val APPLICATION_NATIVE_ADVERTISEMENT_TYPE = "app_native_ad_type"
    private const val APPLICATION_INTERSTITIAL_ADVERTISEMENT_TYPE = "app_interstitial_ad_type"

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }


    var appAdvertisementType: String
        get() = PreferencesManager.getInstance(context)
            .read(fieldName = APPLICATION_ADVERTISEMENT_TYPE)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = APPLICATION_ADVERTISEMENT_TYPE, value = value)


    var appNativeAdvertisementType: String
        get() = PreferencesManager.getInstance(context)
            .read(fieldName = APPLICATION_NATIVE_ADVERTISEMENT_TYPE)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = APPLICATION_NATIVE_ADVERTISEMENT_TYPE, value = value)


    var appInterstitialAdvertisementType: String
        get() = PreferencesManager.getInstance(context)
            .read(fieldName = APPLICATION_INTERSTITIAL_ADVERTISEMENT_TYPE)
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = APPLICATION_INTERSTITIAL_ADVERTISEMENT_TYPE, value = value)
}