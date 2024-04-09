package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import com.application.dao.ImageDao
import com.application.dao.ImageDaoImpl
import com.application.repositories.ProfileImageRepository

class ProfileImageRepositoryImpl(context: Context) : ProfileImageRepository {

    private val imageDAO: ImageDao = ImageDaoImpl(context)
    override suspend fun getProfileImage(id: String): Bitmap? {
        return imageDAO.getImage("profile/${id}.jpeg")
    }

    override suspend fun saveProfileImage(id: String, image: Bitmap) {
        imageDAO.saveImage(image,"profile",id)
    }

    override suspend fun deleteProfileImage(id: String) {
       imageDAO.deleteFile("profile/${id}.jpeg")
    }
}