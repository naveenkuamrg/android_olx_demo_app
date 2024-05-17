package com.application.dao

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.Locale

class ImageDaoImpl(
    private val context: Context
) : ImageDao {

    override suspend fun countImagesInFolder(folderPath: String): Int {
        val folder = File(folderPath)

        val imageExtensions = setOf("jpeg") // Add more extensions if needed
        val imageFiles = folder.listFiles { file ->
            file.isFile && imageExtensions.any {
                file.name.lowercase(
                    Locale.ROOT
                ).endsWith(it)
            }
        }

        return imageFiles?.size ?: 0
    }


    override suspend fun getAllImageFromTheDirectory(folderPath: String): List<Bitmap> {
        val result = mutableListOf<Bitmap>()
        val folder = File(context.filesDir, folderPath)

        val imageExtensions = setOf("jpeg") // Add more extensions if needed
        val imageFiles = folder.listFiles { file ->
            file.isFile && imageExtensions.any {
                file.name.lowercase(
                    Locale.ROOT
                ).endsWith(it)
            }
        }
        if (imageFiles != null) {
            for (file in imageFiles) {
                val inputStream =
                    withContext(Dispatchers.IO) {
                        FileInputStream(file)
                    }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                withContext(Dispatchers.IO) {
                    inputStream.close()
                }
                result.add(bitmap)
            }
        }

        return result
    }

    override suspend fun deleteAllFile(folderPath: String) {
        File(context.filesDir,folderPath).listFiles{ file ->
            file.delete()
        }
    }


    override suspend fun saveImage(bitmap: Bitmap?, path: String, name: String): String {
        val file = File(context.filesDir, path)
        if (!file.exists()) {
            file.mkdirs()
        }
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(File(file, "${name}.jpeg"))
        }
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 99, outputStream)
        withContext(Dispatchers.IO) {
            outputStream.flush()
            outputStream.close()
        }

        return name
    }


    override suspend fun getImage(folderPath: String): Bitmap? {
        return try {
            val inputStream = withContext(Dispatchers.IO) {
                FileInputStream(File(context.filesDir, folderPath))
            }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
            bitmap
        } catch (fileNotFoundException: FileNotFoundException) {
            null
        }
    }

    override suspend fun deleteFile(folderPath: String): Boolean {

        return File(context.filesDir, folderPath).delete()
    }


}