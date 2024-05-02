package com.application.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList

data class ProfileWithWishListProducts(
    @Embedded
    val profile: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "product_id",
        associateBy = Junction(WishList::class)
    )
    val productsDetails: List<ProductDetails>
)