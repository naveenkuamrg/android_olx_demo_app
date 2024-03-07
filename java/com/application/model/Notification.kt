package com.application.model

import java.util.Date



data class Notification(
    val id : Int,
    val content: String,
    val notificationType: NotificationType,
    val timestamp: Date,
    var isRead : Boolean
)