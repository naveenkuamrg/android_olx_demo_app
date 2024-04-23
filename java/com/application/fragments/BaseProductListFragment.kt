package com.application.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.model.ProductListItem
import com.application.viewmodels.ProductListViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseProductListFragment(layout: Int): Fragment(layout),
    ProductRecyclerFragmentCallback {
    protected val productListViewModel: ProductListViewModel by viewModels {
        ProductListViewModel.FACTORY
    }
    protected abstract var recyclerView: RecyclerView
    protected abstract var progressIndicator: CircularProgressIndicator



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecycleView()
    }
    private fun setUpRecycleView() {
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        requireContext().getDrawable(
            R.drawable.recycleview_divider
        )?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        initAdapter()

    }
    final fun initAdapter() {

        val adapter  = ProductSummaryAdapter {
          onProductSummaryClick(it.id)
        }

        if(this is ProductRecyclerFragmentWithFilterCallback){
            adapter.onFilterClickListener = {
                onFilterItemClick(it)
            }
        }

        adapter.addLoadStateListener {
            if(it.refresh == LoadState.Loading){
                progressIndicator.visibility = View.VISIBLE
            }else{
                progressIndicator.visibility = View.GONE

            }
            if (it.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    isListEmpty(true)
                } else {
                    isListEmpty(false)
                }
            }
        }
        adapter.addLoadStateListener {
            Log.i("BaseProductListFragment",it.toString())

        }
        recyclerView.adapter = adapter
    }


    protected fun setData(it: PagingData<ProductListItem>) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            (recyclerView.adapter as ProductSummaryAdapter).submitData(it)
        }
    }


}