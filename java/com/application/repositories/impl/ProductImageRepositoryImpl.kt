package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.application.dao.ImageDAO
import com.application.dao.ImageDaoImpl
import com.application.repositories.ProductImageRepository

class ProductImageRepositoryImpl(context: Context): ProductImageRepository {

    val imageDAO: ImageDAO = ImageDaoImpl(context)
    override suspend fun saveImages(id: Long, images: List<Bitmap>) {
        for ((index,image) in images.withIndex()){
            imageDAO.saveImage(image,id.toString(),index.toString())
        }
    }

    override suspend fun getCountImages(path: String): Int {
        return imageDAO.countImagesInFolder(path)
    }

    override suspend fun getMainImage(path: String): Bitmap? {

        return imageDAO.getImage(path)
    }

    override suspend fun getAllImageFromFile(path: String): List<Bitmap> {
        return imageDAO.getAllImageFromTheDirectory(path)
    }

    override suspend fun deleteAllImageFormFile(path: String) {
            imageDAO.deleteAllFile(path)
    }


}