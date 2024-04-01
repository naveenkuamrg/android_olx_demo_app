package com.application.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.application.entity.InterestedList
import com.application.entity.ProductDetails
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductSummary
import com.application.model.SearchProductResultItem

@Dao
interface ProductDao {
    @Upsert
    fun upsertProductDetails(product: ProductDetails): Long

    @Query("select product_id as productId ,title,postedDate,location,price from product_details where user_id LIKE :userId")
    fun getPostProductSummary(userId: Long): List<ProductSummary>

    @Query("select product_id as productId ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE'  ")
    fun getBuyProductSummary(userId: Long): List<ProductSummary>

    @Query("select product_id as id ,title,price,postedDate,description,availabilityStatus,location,productType,user_id as sellerId," +
            "case when isInterested is null then 0 else 1 end as isInterested ,case when isWishList is null then 0 else 1 end as isWishList from product_details" +
            " left join (select product_id as isInterested from interested_buyers where user_id Like :userId and product_id Like :productId) as isInterested " +
            "left join (select product_id as isWishList from wish_list where user_id Like :userId and product_id Like :productId) as isWishList where product_id Like :productId")
    fun getProduct(productId: Long,userId: Long): Product

    @Delete
    fun deleteProduct(productDetails: ProductDetails): Int

    @Query("UPDATE product_details SET availabilityStatus = :status where product_id = :productId")
    fun  updateProductAvailabilityStatus(productId: Long,status: AvailabilityStatus)

    @Query("insert into interested_buyers values(:userId,:productId)")
    fun insertInterestedList(productId: Long,userId: Long): Long

    @Query("delete from interested_buyers where product_id LIKE :productId and user_id = :userId")
    fun removeInterestedList(productId: Long,userId: Long): Int

@Query("SELECT product_id AS id, title AS name, productType AS type FROM product_details " +
        "WHERE (title LIKE '%' || :searchTerm || '%' OR productType LIKE '%' || :searchTerm || '%') " +
        "AND user_id NOT LIKE :userId AND availabilityStatus = 'AVAILABLE'")
    fun getProductListForSearchResult(searchTerm: String,userId: Long): List<SearchProductResultItem>

}