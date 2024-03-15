package com.application.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

class ImageDAOImpl(
    private val context: Context
) : ImageDAO {


    override suspend fun saveImage(bitmap: Bitmap, name: String): String {
        val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
        Log.i("TAG o",outputStream.toString())
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        withContext(Dispatchers.IO) {
            outputStream.flush()
            outputStream.close()
        }


        return name
    }

    companion object {
        fun determineImageRotation(imageFile: File, bitmap: Bitmap): Bitmap {
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

    override suspend fun getImage(name: String): Bitmap? {
        return try {
            val inputStream = context.openFileInput(name)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
            Log.i("GET",bitmap.toString())
            bitmap
        } catch (fileNotFoundException: FileNotFoundException) {
            null
        }
    }

    override suspend fun deleteImage(name: String): Boolean {
        context.deleteFile(name)
        return true
    }
}