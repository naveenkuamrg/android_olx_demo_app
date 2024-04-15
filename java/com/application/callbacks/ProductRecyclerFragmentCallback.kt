package com.application.callbacks

import com.application.model.ProductType

interface ProductRecyclerFragmentCallback {
    fun onProductSummaryClick(position: Long)

    fun isListEmpty(isEmpty: Boolean)
}

interface ProductRecyclerFragmentWithFilterCallback : ProductRecyclerFragmentCallback {
    fun onFilterItemClick(productType: ProductType)
}


