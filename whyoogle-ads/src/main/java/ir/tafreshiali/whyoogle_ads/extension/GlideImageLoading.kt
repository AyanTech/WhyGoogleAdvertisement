package ir.tafreshiali.whyoogle_ads.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ir.tafreshiali.whyoogle_ads.trying

fun ImageView.loadImageUrl(
    url: String?,
    onImageLoaded: () -> Unit
) {
    if (url == null) {
        this.setImageResource(0)
        return
    }
    trying {
        Glide.with(this.context).load(url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Log.d(
                    "AYAN_ADVERTISEMENT",
                    "cant load the image form internet so hide the container"
                )
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onImageLoaded()
                return false
            }

        }).into(this)
    }
}
