package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.SortBottomSheetCallback
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.viewmodels.ProductListViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

abstract class SortableProductListFragment(layoutId: Int) : BaseProductListFragment(layoutId),
    SortBottomSheetCallback, ProductRecyclerFragmentCallback {


    private var isSortTypeUpdate = false

    protected open fun setObserve(){

        productListViewModel.currentSortType.observe(viewLifecycleOwner) {sort->
            val fragment = childFragmentManager.findFragmentByTag("recyclerView")
            if (fragment is ProductRecycleViewModelCallback) {
                fragment.reassignedAdapter()
            }
            initAdapter()
            when (sort) {
                ProductSortType.POSTED_DATE_DESC -> {
                    productListViewModel.productListPostedDateDESC.observe(viewLifecycleOwner)
                    { pagingData -> setDataToAdapter(pagingData) }
                }

                ProductSortType.POSTED_DATE_ASC -> {
                    productListViewModel.productListPostedDateASC.observe(viewLifecycleOwner)
                    {pagingData -> setDataToAdapter(pagingData) }
                }

                ProductSortType.PRICE_ASC -> {
                    productListViewModel.productListPricesASC.observe(viewLifecycleOwner)
                    {pagingData -> setDataToAdapter(pagingData) }
                }

                ProductSortType.PRICE_DESC -> {
                    productListViewModel.productListPricesDESC.observe(viewLifecycleOwner)
                    {pagingData -> setDataToAdapter(pagingData) }
                }
            }
        }
    }

    private fun setDataToAdapter(data: PagingData<ProductListItem>) {
        setData(data)
    }

     override fun onSortTypeSelected(sortType: ProductSortType) {
         if (productListViewModel.currentSortType.value != sortType) {
             isSortTypeUpdate = false
             productListViewModel.setCurrentProductType(sortType)
         }
     }

}