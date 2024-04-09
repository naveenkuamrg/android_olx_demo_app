package com.application.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.entity.Notification

@Dao
interface NotificationDao {
    @Query("select * from notification where recipientId LIKE :userId")
    fun getNotification(userId: Long): List<Notification>

    @Upsert
    fun upsertNotification(notification: Notification)

    @Query("UPDATE notification SET isRead = 1 WHERE recipientId LIKE :userId ")
    fun updateNotificationIsRead(userId: Long)

}