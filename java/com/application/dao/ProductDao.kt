package com.application.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.entity.ProductDetails
import com.application.model.Product
import com.application.model.ProductSummary

@Dao
interface ProductDao {
    @Upsert
    fun upsertProductDetails(product: ProductDetails): Long

    @Query("select product_id as productId ,title,postedDate,location,price from product_details where user_id LIKE :userId")
    fun getPostProductSummary(userId: Long): List<ProductSummary>

    @Query("select product_id as id ,title,price,postedDate,description,availabilityStatus,location,productType,user_id as sellerId," +
            "case when isInterested is null then 0 else 1 end as isInterested ,case when isWishList is null then 0 else 1 end as isWishList from product_details" +
            " left join (select product_id as isInterested from interested_buyers where user_id Like :userId ) as isInterested " +
            "left join (select product_id as isWishList from wish_list where user_id Like :userId) as isWishList where product_id Like :productId")
    fun getProduct(productId: Long,userId: Long): Product

}