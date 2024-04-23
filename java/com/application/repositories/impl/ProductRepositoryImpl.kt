package com.application.repositories.impl

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import androidx.paging.map
import com.application.AppDatabase
import com.application.dao.NotificationDao
import com.application.dao.ProductDao
import com.application.dao.ProfileDao
import com.application.entity.ProductDetails
import com.application.exceptions.ProductDataException
import com.application.helper.ModelConverter
import com.application.helper.NotificationContentBuilder
import com.application.helper.Utility
import com.application.model.AvailabilityStatus
import com.application.model.NotificationType
import com.application.model.Product
import com.application.model.ProductListItem
import com.application.model.ProductListItem.ProductItem
import com.application.model.ProductType
import com.application.model.ProfileSummary
import com.application.model.SearchProductResultItem
import com.application.repositories.ProductImageRepository
import com.application.repositories.ProductRepository
import com.application.repositories.ProfileImageRepository
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception

class ProductRepositoryImpl(val context: Context) : ProductRepository {

    private val productPaddingConfig = PagingConfig(
        20,
        50,
        enablePlaceholders = false
    )

    private val productDao: ProductDao = AppDatabase.getInstance(context).productDao
    private val profileDao: ProfileDao = AppDatabase.getInstance(context).profileDao
    private val notificationDao: NotificationDao = AppDatabase.getInstance(context).notificationDao

    private val productImageRepository: ProductImageRepository = ProductImageRepositoryImpl(context)
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepositoryImpl(context)


    override suspend fun insertProduct(product: Product): Boolean {
        val productDetails = ModelConverter.productModelToProductDetails(product)
        val id = productDao.upsertProductDetails(productDetails)
        productImageRepository.deleteAllImageFormFile(product.id.toString())
        productImageRepository.saveImages(product.id ?: id, product.images)
        return true
    }

    override fun getProductSummaryDetailsForSellZone(): Flow<PagingData<ProductListItem>> {

        return Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 50
            )
        ) {
            productDao.getPostProductSummary(Utility.getLoginUserId(context))
        }.getFlowPagingData().map {
            it.insertSeparators { productListItem: ProductListItem?, productListItem2: ProductListItem? ->
                if(productListItem != null && productListItem2 != null) {
                    if ((productListItem as ProductItem).availabilityStatus
                        != (productListItem2 as ProductItem).availabilityStatus
                    ) {
                       return@insertSeparators ProductListItem.Divider("Sold Products")
                    }
                }
                null
            }
        }

    }


    override fun getProductSummaryDetailsForBuyZonePostedDateASC(type: ProductType): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceASCWithProductType(
                Utility.getLoginUserId(
                    context
                ),
                type
            )
        }.getFlowPagingData()
    }


    override fun getProductSummaryDetailsForBuyZonePostedDateASC():
            Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPostedDateASC(
                Utility.getLoginUserId(
                    context
                )
            )
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePostedDateDESC(type: ProductType): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPostedDateDESCWithProductType(
                Utility.getLoginUserId(
                    context
                ),
                type
            )
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePostedDateDESC():
            Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPostedDateDESC(
                Utility.getLoginUserId(
                    context
                )
            )
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePriceDESC(type: ProductType): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceDESCWithProductType(
                Utility.getLoginUserId(
                    context
                ),
                type
            )
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePriceDESC(): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceDESC(Utility.getLoginUserId(context))
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePriceASC(type: ProductType): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceASCWithProductType(
                Utility.getLoginUserId(
                    context
                ),
                type
            )
        }.getFlowPagingData()
    }

    override fun getProductSummaryDetailsForBuyZonePriceASC(): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceASC(Utility.getLoginUserId(context))
        }.getFlowPagingData()
    }

    override suspend fun getProductDetailsUsingProductId(productId: Long): Product {
        return productDao.getProductUsingProductId(productId, Utility.getLoginUserId(context))
            .apply {
                images = productImageRepository.getAllImageFromFile(productId.toString())
            }
    }

    override suspend fun getProductDetailsUsingNotificationId(
        notificationId: Long,
        userId: Long
    ): Product {
        try {
            return productDao.getProductUsingNotification(notificationId, userId).apply {
                images = (productImageRepository.getAllImageFromFile(id.toString()))
            }
        } catch (e: Exception) {
            throw ProductDataException.ProductDataDeleteException()
        }


    }

    override suspend fun removeProduct(product: Product): Boolean {

        productImageRepository.deleteAllImageFormFile(product.id.toString())
        return productDao.deleteProduct(
            ModelConverter.productModelToProductDetails(product)
        ) != 0
    }

    override suspend fun updateProductAvailabilityAndNotify(
        product: Product,
        status: AvailabilityStatus,
        productInterestedProfile: List<ProfileSummary>
    ) {

        product.id?.let {
            productDao.updateProductAvailabilityStatus(it, status)
            for (i in productInterestedProfile) {
                notificationDao.upsertNotification(
                    ModelConverter.notificationBuilder(
                        i.id, product.id, NotificationType.PRODUCT,
                        NotificationContentBuilder.build(
                            NotificationType.PRODUCT,
                            Utility.getLoginUserName(context),
                            product.title
                        ), Utility.getLoginUserId(context)
                    )
                )
            }
        }
    }

    override suspend fun updateProductIsInterested(
        userId: Long,
        product: Product,
        isInterested: Boolean
    ): Boolean {
        notificationDao.upsertNotification(
            ModelConverter.notificationBuilder(
                product.sellerId,
                product.id!!, NotificationType.PROFILE,
                NotificationContentBuilder.build(
                    NotificationType.PROFILE,
                    Utility.getLoginUserName(context),
                    product.title,
                    isInterested
                ), Utility.getLoginUserId(context)
            )
        )
        return if (isInterested) {
            productDao.insertInterestedList(product.id, userId) > 0
        } else {
            productDao.removeInterestedList(product.id, userId) > 0
        }
    }

    override suspend fun getInterestedProfile(productId: Long): List<ProfileSummary> {
        return ModelConverter.productsWithInterestedProfileSummary(
            profileDao.getInterestedProfile(productId)
        ).apply {
            forEach { productSummary ->
                productSummary.profileImage =
                    profileImageRepository.getProfileImage(productSummary.id.toString())
            }
        }

    }

    override suspend fun getSearchProduct(
        searchTerm: String,
        userId: Long
    ): List<SearchProductResultItem> {
        return productDao.getProductListForSearchResult(searchTerm, userId)
    }

    override suspend fun updateIsFavorite(product: Product, isFavorite: Boolean, userId: Long) {
        if (isFavorite) {
            productDao.insertIsWishList(ModelConverter.wishListEntityBuilder(product.id!!, userId))
        } else {
            productDao.removeIsWishList(ModelConverter.wishListEntityBuilder(product.id!!, userId))
        }
    }

    override fun getFavouriteProductList(): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getFavouriteProductSummary(Utility.getLoginUserId(context))
        }.getFlowPagingData()
    }


    override fun getInterestedProductList(): Flow<PagingData<ProductListItem>> {

        return Pager(
            productPaddingConfig
        ) {
            productDao.getInterestedProductSummary(Utility.getLoginUserId(context))
        }.getFlowPagingData()
    }

    override suspend fun updateIsContent(userId: Long, productId: Long) {
        productDao.updateIsContent(userId, productId)
    }


    private suspend fun setImg(product: ProductItem) {
        product.image = productImageRepository.getMainImage(
            product.id.toString()
        )
    }

    private fun <Key : Any> Pager<Key, ProductItem>.getFlowPagingData(): Flow<PagingData<ProductListItem>> {
        return this.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }


}