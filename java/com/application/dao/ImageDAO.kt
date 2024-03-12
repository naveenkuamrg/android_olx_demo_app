package com.application.dao

import android.graphics.Bitmap

interface ImageDAO {
    suspend fun saveImage(bitmap: Bitmap, name: String): String
    suspend fun getImage(name: String): Bitmap?
    suspend fun deleteImage(name: String): Boolean
}