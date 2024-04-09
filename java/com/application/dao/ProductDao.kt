package com.application.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.application.entity.ProductDetails
import com.application.entity.WishList
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductListItem.ProductItem
import com.application.model.ProductListItem
import com.application.model.ProductType
import com.application.model.SearchProductResultItem

@Dao
interface ProductDao {
    @Upsert
    fun upsertProductDetails(product: ProductDetails): Long

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id LIKE :userId ")
    fun getPostProductSummary(userId: Long): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' ORDER BY postedDate DESC ")
    fun getBuyProductSummaryOrderByPostedDateDESC(userId: Long): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' ORDER BY postedDate ASC ")
    fun getBuyProductSummaryOrderByPostedDateASC(userId: Long): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' ORDER BY price DESC ")
    fun getBuyProductSummaryOrderByPriceDESC(userId: Long): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' ORDER BY price ASC ")
    fun getBuyProductSummaryOrderByPriceASC(userId: Long): List<ProductItem>

    @Query(
        "select product_id as id ,title,price,postedDate,description,availabilityStatus,location,productType,user_id as sellerId," +
                "case when isInterested is null then 0 else 1 end as isInterested ,case when isWishList is null then 0 else 1 end as isWishList from product_details as dp" +
                " left join (select product_id as isInterested from interested_buyers where user_id Like :userId and product_id Like :productId) as isInterested " +
                "left join (select product_id as isWishList from wish_list where user_id Like :userId and product_id Like :productId) as isWishList where product_id Like :productId "
    )
    fun getProduct(productId: Long, userId: Long): Product

    @Delete
    fun deleteProduct(productDetails: ProductDetails): Int

    @Query("UPDATE product_details SET availabilityStatus = :status where product_id = :productId")
    fun updateProductAvailabilityStatus(productId: Long, status: AvailabilityStatus)

    @Query("insert into interested_buyers values(:userId,:productId)")
    fun insertInterestedList(productId: Long, userId: Long): Long

    @Query("delete from interested_buyers where product_id LIKE :productId and user_id = :userId")
    fun removeInterestedList(productId: Long, userId: Long): Int

    @Query(
        "SELECT product_id AS id, title AS name, productType AS type FROM product_details " +
                "WHERE (title LIKE '%' || :searchTerm || '%' OR productType LIKE  :searchTerm || '%') " +
                "AND user_id NOT LIKE :userId AND availabilityStatus = 'AVAILABLE'"
    )
    fun getProductListForSearchResult(
        searchTerm: String,
        userId: Long
    ): List<SearchProductResultItem>

    @Insert
    fun insertIsWishList(data: WishList)

    @Delete
    fun removeIsWishList(data: WishList)

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' and productType LIKE :type ORDER BY price ASC ")
    fun getBuyProductSummaryOrderByPriceASCWithProductType(userId: Long,type: ProductType): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' and productType LIKE :type ORDER BY postedDate DESC ")
    fun getBuyProductSummaryOrderByPostedDateDESCWithProductType(userId: Long,type: ProductType): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' and productType LIKE :type ORDER BY postedDate ASC ")
    fun getBuyProductSummaryOrderByPostedDateASCWithProductType(userId: Long,type: ProductType): List<ProductItem>

    @Query("select product_id as id ,title,postedDate,location,price from product_details where user_id Not Like :userId and availabilityStatus LIKE 'AVAILABLE' and productType LIKE :type ORDER BY price DESC ")
    fun getBuyProductSummaryOrderByPriceDESCWithProductType(userId: Long,type: ProductType): List<ProductItem>

    @Query("select product_details.product_id as id ,title,postedDate,location,price from wish_list left join product_details on wish_list.product_id == product_details.product_id where wish_list.user_id LIKE :userId")
    fun getFavouriteProductSummary(userId: Long): List<ProductItem>

    @Query("select product_details.product_id as id ,title,postedDate,location,price from interested_buyers left join product_details on interested_buyers.product_id == product_details.product_id where interested_buyers.user_id LIKE :userId")
    fun getInterestedProductSummary(userId: Long): List<ProductItem>



}