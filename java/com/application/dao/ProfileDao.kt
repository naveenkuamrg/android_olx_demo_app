package com.application.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.entity.relations.ProductsWithInterestedProfile

@Dao
interface ProfileDao {

    @Query("select * from product_details where product_id Like :productId ")
    fun getInterestedProfile(productId: Long): ProductsWithInterestedProfile
}