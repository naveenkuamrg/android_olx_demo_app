package com.application.repositories

import com.application.model.Notification

interface NotificationRepository {
    suspend fun  getNotification(userId: Long): List<Notification>

    suspend fun updateNotificationIsReadStatus(userId: Long)
}