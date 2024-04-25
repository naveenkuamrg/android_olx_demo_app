package com.application.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "interested_buyers" , foreignKeys = [
    ForeignKey(User :: class, parentColumns = ["user_id"] ,
        childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
    ForeignKey(ProductDetails :: class , parentColumns = ["product_id"],
        childColumns = ["product_id"] ,onDelete = ForeignKey.CASCADE)
], primaryKeys = ["user_id","product_id"])
data class InterestedList(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("product_id")
    val productId: Long,
    val isContented: Boolean
)