package com.application.repositories.impl

import android.content.Context
import android.graphics.Bitmap
import com.application.AppDatabase
import com.application.helper.ModelConverter

import com.application.model.Notification
import com.application.model.NotificationType
import com.application.repositories.NotificationRepository

class NotificationRepositoryImpl(context: Context) : NotificationRepository {

    private val notificationDao = AppDatabase.getInstance(context).notificationDao
    private val productImageRepository = ProductImageRepositoryImpl(context)
    private val profileImageRepository = ProfileImageRepositoryImpl(context)

    override suspend fun getNotification(userId: Long): List<Notification> {
        val notifications: MutableList<Notification> = mutableListOf()
        notificationDao.getNotification(userId).forEach { notificationEntity ->
            notifications.add(
                ModelConverter.notificationEntityToNotificationModel(
                    notificationEntity
                ).apply {
                    val tempImage = if (type == NotificationType.PRODUCT) {
                        productImageRepository.getMainImage(
                            notificationEntity.productId.toString()
                        )
                    } else {
                        profileImageRepository.getProfileImage(
                            notificationEntity.senderId.toString()
                        )
                    }
                    if (tempImage != null) {
                        image = tempImage
                    }
                })
        }
        return notifications
    }

    override suspend fun updateNotificationIsReadStatus(userId: Long) {
        notificationDao.updateNotificationIsRead(userId)
    }


}