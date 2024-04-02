package com.application.callbacks

import com.application.model.ProductSortType

interface SortBottomSheetCallback {
    fun onClick(sortType: ProductSortType)
}