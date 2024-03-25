package com.application.repositories

import com.application.model.Product
import com.application.model.ProductSummary

interface ProductRepository {
    suspend fun insertProduct(product: Product): Boolean

    suspend fun getProductSummaryDetailsForSellZone(userId: Long): List<ProductSummary>

    suspend fun getProductDetails(productId: Long,userId: Long): Product
}