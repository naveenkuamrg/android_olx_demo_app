package com.application.model

import android.net.Uri
import java.util.Date

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val postedDate: Date,
    val images: List<Uri>,
    val description: String,
    val availabilityStatus: AvailabilityStatus,
    val location: String,
    val productType : ProductType,
    val sellerId : Long,
    val isInterested: Boolean,
    val isWishList : Boolean
)





