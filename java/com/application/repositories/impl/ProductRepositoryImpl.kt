package com.application.repositories.impl

import android.content.Context
import android.util.Log
import com.application.AppDatabase
import com.application.dao.ProductDao
import com.application.helper.ModelConverter
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductSummary
import com.application.repositories.ProductImageRepository
import com.application.repositories.ProductRepository

class ProductRepositoryImpl(val context: Context) : ProductRepository {

    private val productDao: ProductDao = AppDatabase.getInstance(context).productDao
    private val productImageRepository: ProductImageRepository = ProductImageRepositoryImpl(context)
    override suspend fun insertProduct(product: Product): Boolean {
        val productDetails = ModelConverter.productModelToProductDetails(product)
        val id = productDao.upsertProductDetails(productDetails)
        productImageRepository.deleteAllImageFormFile(product.id.toString())
        productImageRepository.saveImages(product.id?: id , product.images)
        return true
    }

    override suspend fun getProductSummaryDetailsForSellZone(userId: Long): List<ProductSummary> {
        val result = productDao.getPostProductSummary(userId)
        for (product in result) {
            product.image =
                productImageRepository.getMainImage(
                    "${product.productId}/0.jpeg"
                )
        }
        return result
    }

    override suspend fun getProductDetails(productId: Long,userId: Long): Product {
         return productDao.getProduct(productId,userId).apply {
             images = (productImageRepository.getAllImageFromFile(productId.toString()))
         }
    }

    override suspend fun removeProduct(product: Product): Boolean {

        productImageRepository.deleteAllImageFormFile(product.id.toString())
        return if(productDao.deleteProduct(ModelConverter.productModelToProductDetails(product)) != 0){
            true
        }else{
            false
        }
    }

    override suspend fun updateProductAvailabilityStatus(product: Product,status: AvailabilityStatus) {
        product.id?.let { productDao.updateProductAvailabilityStatus(it,status) }
    }
}