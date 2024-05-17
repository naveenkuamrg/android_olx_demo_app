package com.application.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.application.entity.InterestedList
import com.application.entity.ProductDetails
import com.application.entity.User

data class ProductsWithInterestedProfile(
    @Embedded val productDetails : ProductDetails,
    @Relation (
        parentColumn = "product_id",
        entityColumn = "user_id",
        associateBy = Junction(InterestedList :: class)
    )
    val profileList : List<User>,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id",
    )
    val isContented: List<InterestedList>

)