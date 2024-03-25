package com.application.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.application.model.AvailabilityStatus
import com.application.model.ProductType
import java.sql.Date


@Entity(tableName = "product_details", foreignKeys = [
    ForeignKey(User :: class , parentColumns = ["user_id"], childColumns = ["user_id"], onDelete =  ForeignKey.CASCADE)
])
data class ProductDetails(
    val title : String,
    val price : Double,
    val postedDate :Long,
    val description : String,
    val availabilityStatus : AvailabilityStatus,
    val location : String,
    val productType: ProductType,
    @ColumnInfo(name = "user_id")
    val ownerId : Long
    ){
    @ColumnInfo(name = "product_id")
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}