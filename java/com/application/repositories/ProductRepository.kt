package com.application.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.application.model.AvailabilityStatus
import com.application.model.Notification
import com.application.model.Product
import com.application.model.ProductListItem
import com.application.model.ProductListItem.ProductItem
import com.application.model.Profile
import com.application.model.SearchProductResultItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.model.ProfileSummary
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insertProduct(product: Product): Boolean

    fun getProductSummaryDetailsForSellZone(): Flow<PagingData<ProductListItem>>


    fun getProductSummaryDetailsForBuyZonePostedDateASC(type: ProductType):
            Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePostedDateDESC(type: ProductType): Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePriceDESC(type: ProductType): Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePriceASC(type: ProductType): Flow<PagingData<ProductListItem>>
    fun getProductSummaryDetailsForBuyZonePostedDateASC(): Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePostedDateDESC(): Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePriceDESC(): Flow<PagingData<ProductListItem>>

    fun getProductSummaryDetailsForBuyZonePriceASC(): Flow<PagingData<ProductListItem>>


    suspend fun getProductDetailsUsingProductId(productId: Long,): Product

    suspend fun getProductDetailsUsingNotificationId(notificationId: Long, userId: Long): Product

    suspend fun removeProduct(product: Product): Boolean

    suspend fun updateProductAvailabilityAndNotify(
        product: Product,
        status: AvailabilityStatus,
        productInterestedProfile: List<ProfileSummary>
    )

    suspend fun updateProductIsInterested(
        userId: Long,
        product: Product,
        isInterested: Boolean
    ): Boolean

    suspend fun getInterestedProfile(productId: Long): List<ProfileSummary>

    suspend fun getSearchProduct(searchTerm: String, userId: Long): List<SearchProductResultItem>

    suspend fun updateIsFavorite(product: Product, isFavorite: Boolean, userId: Long)


    fun getFavouriteProductList(): Flow<PagingData<ProductListItem>>

    fun getInterestedProductList(): Flow<PagingData<ProductListItem>>

    suspend fun updateIsContent(userId: Long,productId: Long)

}