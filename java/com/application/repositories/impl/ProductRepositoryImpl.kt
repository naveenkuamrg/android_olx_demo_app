package com.application.repositories.impl

import android.content.Context
import android.util.Log
import com.application.AppDatabase
import com.application.dao.ProductDao
import com.application.helper.ModelConverter
import com.application.model.Product
import com.application.repositories.ProductImageRepository
import com.application.repositories.ProductRepository

class ProductRepositoryImpl(val context: Context) : ProductRepository {

    private val productDao: ProductDao = AppDatabase.getInstance(context).productDao
    private val productImageRepository: ProductImageRepository = ProductImageRepositoryImpl(context)
    override suspend fun insertProductRepository(product: Product): Boolean {
        val productDetails = ModelConverter.productModelToProductDetails(product)
        val id = productDao.insertProductDetails(productDetails)
        productImageRepository.saveImages(id,product.images)
        return  true
    }

}