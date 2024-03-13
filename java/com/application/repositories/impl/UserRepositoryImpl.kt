package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.application.AppDatabase
import com.application.helper.ModelConverter
import com.application.model.Profile
import com.application.repositories.ImageRepository
import com.application.repositories.UserRepository

class UserRepositoryImpl(context : Context) : UserRepository {
    val dao = AppDatabase.getInstance(context).userDao
    val imageRepository : ImageRepository = ProfileImageRepositoryImpl(context)
    override suspend fun getUserProfile(userId: Long): Profile {
        val profile = ModelConverter.profileFromUserAndUri(dao.getUser(userId))
        profile.profileImage = imageRepository.getProfileImage(profile.id.toString())
        return profile
    }

    override fun updateUserName(name: String,userId: Long) {
        dao.updateUserName(name,userId)
    }

    override fun updateUserEmail(email: String,userId: Long) {
        dao.updateUserEmail(email,userId)
    }

    override fun updateUserPhone(phone: String,userId: Long) {
       dao.updateUserPhone(phone,userId)
    }

    override fun updateUserPassword(id: Long,currentPassword: String,newPassword: String): Int {
       return dao.updateUserPassword(id,currentPassword,newPassword)
    }

    override suspend fun updateProfileImage(image: Bitmap, id: Long) {
        imageRepository.saveProfileImage(id.toString(),image)
    }

    override suspend fun removeProfileImage(id: Long) {
        imageRepository.deleteProfileImage(id.toString())
    }


}