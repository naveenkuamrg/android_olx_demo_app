package com.application.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class ImageDAOImpl(
    private val context: Context
) : ImageDAO {


    override suspend fun saveImage(bitmap: Bitmap, name: String): String {
        val outputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        withContext(Dispatchers.IO) {
            outputStream.flush()
            outputStream.close()
        }
        return name
    }

    override suspend fun getImage(name: String): Bitmap? {
        return try {
            val inputStream = context.openFileInput(name)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
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