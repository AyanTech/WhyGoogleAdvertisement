package ir.tafreshiali.whygoogleadvertisement

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.adivery.sdk.Adivery
import com.adivery.sdk.AdiveryAdListener
import com.adivery.sdk.AdiveryNativeAdView
import ir.tafreshiali.whyoogle_ads.trying


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.marqueeTv)
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


    }
}
