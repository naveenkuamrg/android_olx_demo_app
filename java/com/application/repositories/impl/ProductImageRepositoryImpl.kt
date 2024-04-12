package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import com.application.dao.ImageDao
import com.application.dao.ImageDaoImpl
import com.application.repositories.ProductImageRepository

class ProductImageRepositoryImpl(context: Context): ProductImageRepository {

    private val imageDAO: ImageDao = ImageDaoImpl(context)
    override suspend fun saveImages(id: Long, images: List<Bitmap?>) {
        for ((index,image) in images.withIndex()){
            imageDAO.saveImage(image,id.toString(),index.toString())
        }
    }

    override suspend fun getCountImages(path: String): Int {
        return imageDAO.countImagesInFolder(path)
    }

    override suspend fun getMainImage(path: String): Bitmap? {

        return imageDAO.getImage("$path/0.jpeg")
    }

    override suspend fun getAllImageFromFile(path: String): List<Bitmap> {
        return imageDAO.getAllImageFromTheDirectory(path)
    }

    override suspend fun deleteAllImageFormFile(path: String) {
            imageDAO.deleteAllFile(path)
    }


}