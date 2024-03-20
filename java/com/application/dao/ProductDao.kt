package com.application.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.application.entity.ProductDetails

@Dao
interface ProductDao {
    @Insert
    fun insertProductDetails(product: ProductDetails): Long


}