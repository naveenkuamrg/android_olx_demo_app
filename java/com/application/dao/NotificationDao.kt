package com.application.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.entity.Notification

@Dao
interface NotificationDao {
    @Query("select * from notification where recipientId LIKE :userId order by timestamp DESC")
    fun getNotification(userId: Long): List<Notification>

    @Upsert
    fun upsertNotification(notification: Notification)

    @Query("UPDATE notification SET isRead = 1 WHERE recipientId LIKE :userId ")
    fun updateNotificationIsRead(userId: Long)

    @Query("select COUNT(id) > 0  from notification where" +
            " recipientId LIKE :userId AND isRead LIKE 0 ")
    fun getIsUnreadNotification(userId: Long): Boolean
}