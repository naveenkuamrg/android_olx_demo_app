package com.application.repositories

import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductListItem.ProductItem
import com.application.model.Profile
import com.application.model.SearchProductResultItem
import com.application.model.ProductSortType
import com.application.model.ProductType

interface ProductRepository {
    suspend fun insertProduct(product: Product): Boolean

    suspend fun getProductSummaryDetailsForSellZone(userId: Long): List<ProductItem>

    suspend fun getProductSummaryDetailsForBuyZone(userId: Long,type: ProductType,sort: ProductSortType): List<ProductItem>

    suspend fun getProductSummaryDetailsForBuyZone(
        userId: Long,
        sort: ProductSortType
    ): List<ProductItem>

    suspend fun getProductDetails(productId: Long, userId: Long): Product

    suspend fun removeProduct(product: Product): Boolean

    suspend fun updateProductAvailabilityAndNotify(
        product: Product,
        status: AvailabilityStatus,
        productInterestedProfile: List<Profile>
    )

    suspend fun updateProductIsInterested(
        userId: Long,
        product: Product,
        isInterested: Boolean
    ): Boolean

    suspend fun getInterestedProfile(productId: Long): List<Profile>

    suspend fun getSearchProduct(searchTerm: String, userId: Long): List<SearchProductResultItem>

    suspend fun updateIsFavorite(product: Product,isFavorite: Boolean,userId: Long)


    suspend fun getFavouriteProductList(userId: Long): List<ProductItem>

    suspend fun getInterestedProductList(userId: Long): List<ProductItem>

}