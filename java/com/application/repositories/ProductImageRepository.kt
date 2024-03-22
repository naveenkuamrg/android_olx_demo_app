package com.application.repositories

import android.graphics.Bitmap
import com.application.model.Product

interface ProductImageRepository {
    suspend fun saveImages(id: Long,images: List<Bitmap>)

    suspend fun getCountImages(path: String): Int

    suspend fun getMainImage(path: String) : Bitmap?
}