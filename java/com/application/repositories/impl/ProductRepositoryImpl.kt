package com.application.repositories.impl

import android.content.Context
import com.application.AppDatabase
import com.application.dao.ProductDao
import com.application.dao.ProfileDao
import com.application.helper.ModelConverter
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductSortType
import com.application.model.ProductSummary
import com.application.model.Profile
import com.application.model.SearchProductResultItem
import com.application.repositories.ProductImageRepository
import com.application.repositories.ProductRepository
import com.application.repositories.ProfileImageRepository

class ProductRepositoryImpl(val context: Context) : ProductRepository {


    private val productDao: ProductDao = AppDatabase.getInstance(context).productDao
    private val profileDao: ProfileDao = AppDatabase.getInstance(context).profileDao

    private val productImageRepository: ProductImageRepository = ProductImageRepositoryImpl(context)
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepositoryImpl(context)
    override suspend fun insertProduct(product: Product): Boolean {
        val productDetails = ModelConverter.productModelToProductDetails(product)
        val id = productDao.upsertProductDetails(productDetails)
        productImageRepository.deleteAllImageFormFile(product.id.toString())
        productImageRepository.saveImages(product.id ?: id, product.images)
        return true
    }

    override suspend fun getProductSummaryDetailsForSellZone(userId: Long): List<ProductSummary> {
        val result = productDao.getPostProductSummary(userId)
        setImage(result)
        return result
    }

    override suspend fun getProductSummaryDetailsForBuyZone(userId: Long, sort: ProductSortType):
            List<ProductSummary> {
        val productList = when (sort) {
            ProductSortType.POSTED_DATE_ASC -> {
                productDao.getBuyProductSummaryOrderByPostedDateASC(userId)
            }

            ProductSortType.POSTED_DATE_DESC -> {
                productDao.getBuyProductSummaryOrderByPostedDateDESC(userId)
            }

            ProductSortType.PRICE_ASC -> {
                productDao.getBuyProductSummaryOrderByPriceASC(userId)
            }

            ProductSortType.PRICE_DESC -> {
                productDao.getBuyProductSummaryOrderByPriceDESC(userId)
            }
        }

        return productList.also {
            setImage(it)
        }
    }

    override suspend fun getProductDetails(productId: Long, userId: Long): Product {

        return productDao.getProduct(productId, userId).apply {
            images = (productImageRepository.getAllImageFromFile(productId.toString()))
        }
    }

    override suspend fun removeProduct(product: Product): Boolean {

        productImageRepository.deleteAllImageFormFile(product.id.toString())
        return productDao.deleteProduct(
            ModelConverter.productModelToProductDetails(product)
        ) != 0
    }

    override suspend fun updateProductAvailabilityStatus(
        product: Product,
        status: AvailabilityStatus
    ) {
        product.id?.let { productDao.updateProductAvailabilityStatus(it, status) }
    }

    override suspend fun updateProductIsInterested(
        userId: Long,
        productId: Long,
        isInterested: Boolean
    ): Boolean {
        return if (isInterested) {
            productDao.insertInterestedList(productId, userId) > 0
        } else {
            productDao.removeInterestedList(productId, userId) > 0
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

    private suspend fun setImage(listOfProductSummary: List<ProductSummary>) {
        for (product in listOfProductSummary) {
            product.image =
                productImageRepository.getMainImage(
                    "${product.productId}/0.jpeg"
                )
        }
    }
}