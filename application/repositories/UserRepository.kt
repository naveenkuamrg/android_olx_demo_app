package com.application.repositories

import android.graphics.Bitmap
import com.application.model.Profile

interface UserRepository {
    suspend fun getUserProfile(userId : Long): Profile

    fun updateUserName(name: String,userId: Long )

    fun updateUserEmail(email: String,userId: Long)

    fun updateUserPhone(phone: String,userId: Long)

    fun updateUserPassword(id: Long,currentPassword: String,newPassword: String): Int

    suspend fun updateProfileImage(image: Bitmap, id: Long)

    suspend fun removeProfileImage(id: Long)

}