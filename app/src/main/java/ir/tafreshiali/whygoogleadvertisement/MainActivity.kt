package ir.tafreshiali.whygoogleadvertisement

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.initialization.AdapterStatus
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisement
import ir.tafreshiali.whyoogle_ads.admob.AdmobAdvertisementImpl
import ir.tafreshiali.whyoogle_ads.extension.loadAdmobNativeAdvertisementView


class MainActivity : AppCompatActivity(),
    AdmobAdvertisement by AdmobAdvertisementImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.marqueeTv)
       // val adView = findViewById<LinearLayout>(R.id.nativeLl)
        tv.isSelected = true

        //val adiveryNativeAdView = findViewById<AdiveryNativeAdView>(R.id.native_ad_view)

        /*        Adivery.configure(application, "ad8af967-1e88-4d72-95c6-1cd5f955230d")

                trying {

                    val adiveryNativeAdView = AdiveryNativeAdView(applicationContext)
                    adiveryNativeAdView.setNativeAdLayout(R.layout.ad_layout_example)
                    adiveryNativeAdView.setPlacementId("04b32db2-a991-433b-844b-cad003f74693")
                    val a = adiveryNativeAdView.findViewById<TextView>(R.id.adivery_headline)
                    a?.isSelected = true


                    adiveryNativeAdView.setListener(object : AdiveryAdListener() {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                        }

                        override fun onAdShown() {
                            super.onAdShown()
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                        }

                        override fun onError(reason: String?) {
                            super.onError(reason)

                        }
                    })

                    adiveryNativeAdView?.loadAd()

                }*/

/*        MobileAds.initialize(this)

        loadNativeAdLoader(
            application,
            "ca-app-pub-7289139744252145~4028687450",
            onNativeAdLoaded = { admobNativeAd ->
                adView.loadAdmobNativeAdvertisementView(
                    nativeAd = admobNativeAd,
                    admobNativeLayoutId = ir.tafreshiali.whyoogle_ads.R.layout.admob_simple_native_ad,
                    onViewReady = {
                        //  adView.makeVisible()
                        // onAdLoaded?.invoke()
                    }
                )
            },
            onNativeAdFailed = {
                // adView.makeGone()
                Log.d("Admob", "Loading Native Ad Is Failed")
            })*/
    }
}
