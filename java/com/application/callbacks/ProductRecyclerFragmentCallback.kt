package com.application.callbacks

import com.application.model.ProductType

interface ProductRecyclerFragmentCallback {
    fun onProductSummaryClick(productId: Long)

    fun isListEmpty(isEmpty: Boolean)

}

interface ProductRecyclerFragmentWithFilterCallback : ProductRecyclerFragmentCallback {
    fun onFilterItemClick(productType: ProductType)
}


