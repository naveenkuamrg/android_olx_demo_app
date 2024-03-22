package com.application.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.application.entity.ProductDetails
import com.application.model.ProductSummary

@Dao
interface ProductDao {
    @Insert
    fun insertProductDetails(product: ProductDetails): Long

    @Query("select product_id as productId ,title,postedDate,location from product_details where user_id LIKE :userId")
    fun getProductSummary(userId: Long): List<ProductSummary>

}