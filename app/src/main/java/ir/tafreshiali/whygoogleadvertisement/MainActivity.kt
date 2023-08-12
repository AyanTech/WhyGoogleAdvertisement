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

    }
}