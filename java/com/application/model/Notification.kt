package com.application.model

import android.graphics.Bitmap
import androidx.room.Ignore


data class Notification(
    val id : Long,
    val timestamp: Long,
    val isRead : Boolean,
    val content: String,
    val type: NotificationType,
)