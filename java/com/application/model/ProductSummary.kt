package com.application.model

import android.graphics.Bitmap
import android.net.Uri
import java.util.Date

data class ProductSummary(
    val productId: Long,
    val title: String,
    val postedDate: String,
    val location: String,
){

}
