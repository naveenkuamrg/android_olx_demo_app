package com.application.callbacks

import com.application.model.ProductSummary

interface ProductRecycleViewFragmentCallback {
    fun setData(data: List<ProductSummary>)

    fun getData(): List<ProductSummary>
}