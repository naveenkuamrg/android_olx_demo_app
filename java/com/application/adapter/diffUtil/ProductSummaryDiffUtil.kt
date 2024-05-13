package com.application.adapter.diffUtil

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.application.model.ProductListItem

class ProductSummaryDiffUtil: DiffUtil.ItemCallback<ProductListItem>() {
    override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
        Log.i("MAP1",Thread.currentThread().toString())
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductListItem,
        newItem: ProductListItem
    ): Boolean {
        Log.i("MAP1",Thread.currentThread().toString())

        return oldItem == newItem
    }
}