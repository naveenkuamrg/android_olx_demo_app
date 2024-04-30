package com.application.repositories.impl

import android.content.Context
import com.application.AppDatabase
import com.application.helper.ModelConverter

import com.application.model.Notification
import com.application.repositories.NotificationRepository

class NotificationRepositoryImpl(context: Context) : NotificationRepository {

    private val notificationDao = AppDatabase.getInstance(context).notificationDao
    private val productImageRepository = ProductImageRepositoryImpl(context)

    override suspend fun getNotification(userId: Long): List<Notification> {
        val notifications: MutableList<Notification> = mutableListOf()
        notificationDao.getNotification(userId).forEach {
            notifications.add(ModelConverter.notificationEntityToNotificationModel(it).apply {
                val tempImage = productImageRepository.getMainImage(it.productId.toString())
                if(tempImage != null) {
                    image = tempImage
                }
            })
        }
        return notifications
    }

    override suspend fun updateNotificationIsReadStatus(userId: Long){
        notificationDao.updateNotificationIsRead(userId)
    }


}