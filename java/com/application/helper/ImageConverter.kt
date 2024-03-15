package com.application.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.FileNotFoundException
import java.io.IOException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition


object ImageConverter {
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val resolver: ContentResolver = context.contentResolver
            val inputStream = resolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }


    fun loadBitmapFromUri(
        context: Context,
        uri: Uri,
        targetWidth: Int,
        targetHeight: Int,
        callback: (Bitmap?) -> Unit
    ) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .apply(
                RequestOptions()
                    .override(targetWidth, targetHeight) // Set target width and height
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
            )
            .into(object : CustomTarget<Bitmap>() {

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    callback(null)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }

            })
    }
}