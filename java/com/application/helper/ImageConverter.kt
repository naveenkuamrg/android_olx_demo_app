package com.application.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.FileDescriptor
import java.io.FileNotFoundException
import java.io.IOException

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
}