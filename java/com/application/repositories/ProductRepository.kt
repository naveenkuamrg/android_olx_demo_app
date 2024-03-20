package com.application.repositories

import com.application.model.Product

interface ProductRepository {
    suspend fun insertProductRepository(product: Product): Boolean
}