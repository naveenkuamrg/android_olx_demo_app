package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.adapter.ProductSummaryAdapterWithFilter
import com.application.callbacks.OnFilterItemClickListener
import com.application.callbacks.OnItemClickListener
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.model.ProductListItem
import com.application.model.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view),
    OnFilterItemClickListener, ProductRecycleViewModelCallback {

    lateinit var callback: OnItemClickListener
    lateinit var binding: FragmentProductRecycleViewBinding
    lateinit var adapter: ProductSummaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductRecycleViewBinding.bind(view)

        setUpRecycleView()
        callback = parentFragment as OnItemClickListener
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
        adapter = if (arguments?.getBoolean("isFilterEnable") == true) {
            ProductSummaryAdapterWithFilter(this) {
                callback.onItemClick(it.id.toInt())
            }
        } else {
            ProductSummaryAdapter {
                callback.onItemClick(it.id.toInt())
            }
        }
        recyclerView.adapter = adapter

    }

    override fun onFilterItemClick(productType: ProductType) {
        (callback as OnFilterItemClickListener).onFilterItemClick(productType)
    }

    override fun onItemClick(position: Int) {
        callback.onItemClick(position)
    }

    override fun reassignedAdapter() {

        adapter = if (arguments?.getBoolean("isFilterEnable") == true) {
                ProductSummaryAdapterWithFilter(this) {
                    callback.onItemClick(it.id.toInt())
                }
            } else {
                ProductSummaryAdapter {
                    callback.onItemClick(it.id.toInt())
                }
            }
        binding.productRecycleView.adapter = adapter
    }

    override fun onSetData(list: PagingData<ProductListItem>) {
       list.map {
           Log.i("onSetData",it.toString())
       }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            adapter.submitData(list)
        }
        binding.progressCircular.visibility = View.GONE
    }

    companion object {
        fun getInstance(isFilterEnable: Boolean):ProductRecycleViewFragment{
            return ProductRecycleViewFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("isFilterEnable",isFilterEnable)
                }
            }
        }
    }
}