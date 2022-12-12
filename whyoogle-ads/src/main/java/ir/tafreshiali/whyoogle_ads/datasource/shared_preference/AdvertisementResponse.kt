package ir.tafreshiali.whyoogle_ads.datasource.shared_preference

import android.annotation.SuppressLint
import android.content.Context
import ir.ayantech.whygoogle.helper.PreferencesManager


@SuppressLint("StaticFieldLeak")
object AdvertisementResponse {

    private const val advertisementKey = "advertisementResponse"

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    var appAdvertisementResponse: String
        get() = PreferencesManager.getInstance(context)
            .read(fieldName = advertisementKey, defaultValue = "")
        set(value) = PreferencesManager.getInstance(context)
            .save(fieldName = advertisementKey, value = value)

}