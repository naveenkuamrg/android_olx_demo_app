package com.application.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.application.entity.Notification
import com.application.entity.ProductDetails

data class NotificationWithProductDetails(
    @Embedded
    val notification : Notification,
    @Relation(
        parentColumn = "productId",
        entityColumn = "product_id"
    )
    val productDetails: ProductDetails
)