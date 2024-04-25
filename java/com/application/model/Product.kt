package com.application.model

import android.graphics.Bitmap
import androidx.room.Ignore

data class Product(
    val id: Long?,
    val title: String,
    val price: Double,
    val postedDate: Long,
    val description: String,
    var availabilityStatus: AvailabilityStatus,
    val location: String,
    val productType : ProductType,
    val sellerId : Long,
    var isInterested: Boolean = false,
    var isWishList : Boolean = false
){

    @Ignore
    lateinit var images: List<Bitmap>
}





