package com.application.model

import android.graphics.Bitmap


data class Notification(
    val id : Long,
    val timestamp: Long,
    val isRead : Boolean,
    val content: String
){
    lateinit var image: Bitmap
}