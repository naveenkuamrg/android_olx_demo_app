package com.application.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.application.entity.InterestedList
import com.application.entity.ProductDetails
import com.application.entity.User

data class ProfileWithInterestedProducts(
    @Embedded
    val user : User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "product_id",
        associateBy = Junction(InterestedList :: class)
    )
    val productsDetails: List<ProductDetails>
)

