package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.callbacks.ProductRecyclerFragmentCallback
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.callbacks.ProductRecyclerFragmentWithFilterCallback
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.model.ProductListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view),
    ProductRecycleViewModelCallback {

    lateinit var callback: ProductRecyclerFragmentCallback
    lateinit var binding: FragmentProductRecycleViewBinding
    lateinit var adapter: ProductSummaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductRecycleViewBinding.bind(view)

        setUpRecycleView()
        callback = parentFragment as ProductRecyclerFragmentCallback
    }


    private fun setUpRecycleView() {
        val recyclerView = binding.productRecycleView
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
        setAdapter()

    }


    override fun reassignedAdapter() {
        setAdapter()
    }

    private fun setAdapter() {
         adapter = ProductSummaryAdapter {
             Log.i("TAG","onClickLisner")
            callback.onProductSummaryClick(it.id)
        }

        if (arguments?.getBoolean("isFilterEnable") == true) {
            Log.i("TAG","check is Filtere")
            adapter.onFilterClickListener = {
                Log.i("TAG","check")
                (callback as ProductRecyclerFragmentWithFilterCallback).onFilterItemClick(it)
            }
        }
        adapter.addLoadStateListener {
            if (it.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) {
                    callback.isListEmpty(true)
                } else {
                    callback.isListEmpty(false)
                }
            }
        }
        binding.productRecycleView.adapter = adapter
    }

    override fun onSetData(list: PagingData<ProductListItem>) {
        Log.i("PagingData list", list.toString())
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            adapter.submitData(list)
        }
        binding.progressCircular.visibility = View.GONE
    }

    companion object {
        fun getInstance(isFilterEnable: Boolean): ProductRecycleViewFragment {
            return ProductRecycleViewFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("isFilterEnable", isFilterEnable)
                }
            }
        }
    }
}