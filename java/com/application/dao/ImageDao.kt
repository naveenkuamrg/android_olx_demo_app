package com.application.dao

import android.graphics.Bitmap

interface ImageDao {
    suspend fun saveImage(bitmap: Bitmap,path: String, name: String): String

    suspend fun getImage(folderPath: String): Bitmap?

    suspend fun deleteFile(folderPath: String): Boolean

    suspend fun countImagesInFolder(folderPath: String): Int

    suspend fun getAllImageFromTheDirectory(folderPath: String): List<Bitmap>

    suspend fun deleteAllFile(folderPath: String)
}