package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.application.dao.ImageDAO
import com.application.dao.ImageDAOImpl
import com.application.repositories.ProductImageRepository

class ProductImageRepositoryImpl(context: Context): ProductImageRepository {

    val imageDAO: ImageDAO = ImageDAOImpl(context)
    override suspend fun saveImages(id: Long, images: List<Bitmap>) {
        for ((index,image) in images.withIndex()){
            imageDAO.saveImage(image,id.toString(),index.toString())
        }
    }

    override suspend fun getCountImages(path: String): Int {
        Log.i("TAG path",path)
        return imageDAO.countImagesInFolder(path)
    }

}