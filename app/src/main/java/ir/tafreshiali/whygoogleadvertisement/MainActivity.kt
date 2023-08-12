package ir.tafreshiali.whygoogleadvertisement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import ir.tafreshiali.whyoogle_ads.AdvertisementCore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.marqueeTv)
        tv.isSelected = true

        AdvertisementCore.initialize(
            application = application,
            appKey = "ad8af967-1e88-4d72-95c6-1cd5f955230d",
            interstitialAdUnitID = "ca32931a-2c9e-4e81-901b-878697a26d65",
            bannerAdUnitID = "56292dc7-84c0-4b34-b746-3d93406940fd",
            nativeAdUnitID = "04b32db2-a991-433b-844b-cad003f74693",
        )



    }
}