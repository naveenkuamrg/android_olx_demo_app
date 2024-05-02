package com.application.callbacks
import com.application.model.ProductSortType

interface SortBottomSheetCallback {
    fun onSortTypeSelected(sortType: ProductSortType)

    fun getCurrentSortType(): ProductSortType
}

