package com.application.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.application.model.NotificationType
import java.sql.Date

@Entity(
    tableName = "notification", foreignKeys = [
        ForeignKey(
            User::class,
            parentColumns = ["user_id"],
            childColumns = ["recipientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Notification(
    val timestamp: Long,
    val isRead: Boolean,
    val recipientId: Long,
    val productId: Long,
    val content: String,
    val type: NotificationType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

