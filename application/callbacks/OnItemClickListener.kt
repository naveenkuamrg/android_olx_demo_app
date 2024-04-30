package com.application.callbacks

import com.application.model.ProductType

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

interface OnFilterItemClickListener : OnItemClickListener {
    fun onFilterItemClick(productType: ProductType)
}


