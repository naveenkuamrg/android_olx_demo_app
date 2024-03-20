package com.application.repositories

import android.graphics.Bitmap

interface ProductImageRepository {
    suspend fun saveImages(id: Long,images: List<Bitmap>)

    suspend fun getCountImages(path: String): Int
}