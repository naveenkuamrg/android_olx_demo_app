package com.application.model

import android.graphics.Bitmap

import androidx.room.Ignore


data class ProductSummary(
    val productId: Long,
    val title: String,
    val postedDate: String,
    val location: String,
    val price: String,

){
    @Ignore
    var image: Bitmap? = null
}
