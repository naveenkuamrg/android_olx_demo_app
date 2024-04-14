package com.application.callbacks

import androidx.paging.PagingData
import com.application.model.ProductListItem


interface ProductRecycleViewModelCallback {

    fun reassignedAdapter()
    fun onSetData(list: PagingData<ProductListItem>)

}