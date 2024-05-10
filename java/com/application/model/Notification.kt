package com.application.model

import android.graphics.Bitmap
import androidx.room.Ignore
import java.net.URL


data class Notification(
    val id : Long,
    val timestamp: Long,
    val isRead : Boolean,
    val content: String,
    val type: NotificationType,
)