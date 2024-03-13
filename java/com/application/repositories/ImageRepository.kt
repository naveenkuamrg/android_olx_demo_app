package com.application.repositories

import android.graphics.Bitmap

interface ImageRepository {
    suspend fun getProfileImage(id: String): Bitmap?
    suspend fun saveProfileImage(id: String ,image: Bitmap)

    suspend fun deleteProfileImage(id: String)
}