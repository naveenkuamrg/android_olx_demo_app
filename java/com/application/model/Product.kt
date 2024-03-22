package com.application.model

import android.graphics.Bitmap
import java.util.Date

data class Product(
    val id: Long?,
    val title: String,
    val price: Double,
    val postedDate: Date,
    val images: List<Bitmap>,
    val description: String,
    val availabilityStatus: AvailabilityStatus,
    val location: String,
    val productType : ProductType,
    val sellerId : Long,
){
    val isInterested: Boolean? = null
    val isWishList : Boolean? = null
}





