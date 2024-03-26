package com.application.model

import android.graphics.Bitmap
import androidx.room.Ignore
import java.util.Date

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
    val isInterested: Boolean? = null,
    val isWishList : Boolean? = null
){
    @Ignore
    lateinit var images: List<Bitmap>
}





