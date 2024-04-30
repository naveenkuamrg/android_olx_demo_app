package com.application.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "wish_list", foreignKeys = [
    ForeignKey(User :: class , parentColumns = ["user_id"], childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(ProductDetails :: class, parentColumns = ["product_id"], childColumns = ["product_id"],
        onDelete = ForeignKey.CASCADE)
], primaryKeys = ["user_id","product_id"])
data class WishList(
    @ColumnInfo(name = "user_id")var userId: Long,
    @ColumnInfo(name = "product_id")var productId: Long
)