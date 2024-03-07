package com.application.model

import android.net.Uri
import java.util.Date

data class ProductSummary(
    val productId: Long,
    val title: String,
    val postedDate: Date,
    val location: String,
    val isInterested: Boolean,
    val isWishList : Boolean
){
    lateinit var mainImage: Uri
}
