package com.application.callbacks

import com.application.model.ProductListItem.ProductItem

interface ProductRecycleViewModelCallback {

    fun onSetData(list: List<ProductItem>)

}
