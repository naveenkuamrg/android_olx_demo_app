package com.application.repositories.impl

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.AppDatabase
import com.application.dao.NotificationDao
import com.application.dao.ProductDao
import com.application.dao.ProfileDao
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
import com.application.model.Profile
import com.application.model.SearchProductResultItem
import com.application.repositories.ProductImageRepository
import com.application.repositories.ProductRepository
import com.application.repositories.ProfileImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class ProductRepositoryImpl(val context: Context) : ProductRepository {

    val productPaddingConfig = PagingConfig(
        8,
        2,
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
                pageSize = 8,
                enablePlaceholders = false,
                prefetchDistance = 2
            )
        ) {
            productDao.getPostProductSummary(Utility.getLoginUserId(context))
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                Log.i("naveen",it.toString())
                it

            }
        }
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }

    override fun getProductSummaryDetailsForBuyZonePriceDESC(): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceDESC(Utility.getLoginUserId(context))
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
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
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }

    override fun getProductSummaryDetailsForBuyZonePriceASC(): Flow<PagingData<ProductListItem>> {
        return Pager(
            productPaddingConfig
        ) {
            productDao.getBuyProductSummaryOrderByPriceASC(Utility.getLoginUserId(context))
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }
    override suspend fun getProductDetailsUsingProductId(productId: Long, userId: Long): Product {
        return productDao.getProductUsingProductId(productId, userId).apply {
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
        productInterestedProfile: List<Profile>
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
                        )
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
                )
            )
        )
        return if (isInterested) {
            productDao.insertInterestedList(product.id, userId) > 0
        } else {
            productDao.removeInterestedList(product.id, userId) > 0
        }
    }

    override suspend fun getInterestedProfile(productId: Long): List<Profile> {
        val profiles: MutableList<Profile> = mutableListOf()
        profileDao.getInterestedProfile(productId).profileList.map {
            profiles.add(ModelConverter.profileFromUserAndUri(it).apply {
                profileImage = profileImageRepository.getProfileImage(it.id.toString())
            })
        }
        return profiles
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
            PagingConfig(
                10,
                8,
                enablePlaceholders = false
            )
        ) {
            productDao.getFavouriteProductSummary(Utility.getLoginUserId(context))
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }

    override fun getInterestedProductList(): Flow<PagingData<ProductListItem>> {
        return Pager(
            PagingConfig(
                10,
                8,
                enablePlaceholders = false
            )
        ) {
            productDao.getInterestedProductSummary(Utility.getLoginUserId(context))
        }.flow.map { pagingData ->
            pagingData.map {
                setImg(it)
                it
            }
        }
    }

    private suspend fun setImage(listOfProductSummary: List<ProductItem>) {
        for (product in listOfProductSummary) {
            product.image =
                productImageRepository.getMainImage(
                    product.id.toString()
                )
        }
    }

    private suspend fun setImg(product: ProductItem) {
        product.image = productImageRepository.getMainImage(
            product.id.toString()
        )
    }
}