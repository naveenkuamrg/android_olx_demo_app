package com.application.callbacks

import androidx.paging.PagingData
import com.application.model.ProductListItem.ProductItem

interface ProductRecycleViewModelCallback {

    fun onSetData(list: List<ProductItem>)

}
interface ProductRecycleViewModelCallback1 {

    fun onSetData(list: PagingData<ProductItem>)

}