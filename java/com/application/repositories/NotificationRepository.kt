package com.application.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.application.model.Notification

interface NotificationRepository {
     fun  getNotification(): LiveData<PagingData<Notification>>

    suspend fun updateAllNotificationIsReadStatus()

    suspend fun isUnreadNotification(): Boolean

    suspend fun updateNotificationIsReadStatus(notificationId: Long)

}