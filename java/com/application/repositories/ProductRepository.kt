package com.application.repositories

import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductSummary
import com.application.model.Profile
import com.application.model.SearchProductResultItem

interface ProductRepository {
    suspend fun insertProduct(product: Product): Boolean

    suspend fun getProductSummaryDetailsForSellZone(userId: Long): List<ProductSummary>

    suspend fun getProductSummaryDetailsForBuyZone(userId: Long): List<ProductSummary>

    suspend fun getProductDetails(productId: Long, userId: Long): Product

    suspend fun removeProduct(product: Product): Boolean

    suspend fun updateProductAvailabilityStatus(product: Product, status: AvailabilityStatus)

    suspend fun updateProductIsInterested(
        userId: Long,
        productId: Long,
        isInterested: Boolean
    ): Boolean

    suspend fun getInterestedProfile(productId: Long): List<Profile>

    suspend fun getSearchProduct(searchTerm: String,userId: Long): List<SearchProductResultItem>
}