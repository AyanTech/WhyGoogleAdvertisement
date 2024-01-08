package ir.tafreshiali.whyoogle_ads.processor

import android.app.Activity
import android.app.Application
import com.google.android.ump.ConsentInformation
import ir.ayantech.ayannetworking.api.AyanApi
import ir.ayantech.ayannetworking.api.OnChangeStatus
import ir.ayantech.ayannetworking.api.OnFailure
import ir.ayantech.whygoogle.helper.SimpleCallBack
import java.util.concurrent.atomic.AtomicBoolean

interface AdvertisementInitializerProcessor {

    var consentInformation: ConsentInformation?
    var isMobileAdsInitializeCalled: AtomicBoolean
        get() = AtomicBoolean(false)
        set(value) {
            isMobileAdsInitializeCalled = value
        }
    val isPrivacyOptionsRequired: Boolean
        get() =
            consentInformation?.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    fun appAdvertisementGeneralProcessor(
        activity: Activity,
        ayanApi: AyanApi,
        application: Application,
        checkGDPR: Boolean = true,
        changeStatus: OnChangeStatus? = null,
        failure: OnFailure? = null,
        onSourceInitialized: (Boolean) -> Unit,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit
    )

    @Deprecated(message = "This method is deprecated")
    fun appAdvertisementGeneralProcessor(
        ayanApi: AyanApi,
        application: Application,
        changeStatus: OnChangeStatus? = null,
        failure: OnFailure? = null,
        onSourceInitialized: (Boolean) -> Unit,
        updateAppGeneralAdvertisementStatus: (Boolean) -> Unit
    )

    fun initializeMobileAdsSdk(activity: Activity, callback: SimpleCallBack)
}