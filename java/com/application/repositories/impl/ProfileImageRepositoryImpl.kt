package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import com.application.dao.ImageDAO
import com.application.dao.ImageDAOImpl
import com.application.repositories.ImageRepository

class ProfileImageRepositoryImpl(context: Context) : ImageRepository {

    val imageDAO: ImageDAO = ImageDAOImpl(context)
    override suspend fun getProfileImage(id: String): Bitmap? {
        return imageDAO.getImage(id)
    }

    override suspend fun saveProfileImage(id: String, image: Bitmap) {
        imageDAO.saveImage(image, id)
    }

    override suspend fun deleteProfileImage(id: String) {
       imageDAO.deleteImage(id)
    }
}