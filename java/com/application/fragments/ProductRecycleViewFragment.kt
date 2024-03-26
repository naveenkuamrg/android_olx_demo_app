package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.callbacks.ProductSummaryAdapterCallBack
import com.application.callbacks.RecycleProductViewCallback
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.model.ProductSummary
import com.application.viewmodels.ProductRecycleViewModel

class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view),
    ProductSummaryAdapterCallBack {

    val productRecycleViewModel: ProductRecycleViewModel by viewModels {
        ProductRecycleViewModel.FACTORY
    }
    lateinit var callback: RecycleProductViewCallback
    lateinit var binding: FragmentProductRecycleViewBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductRecycleViewBinding.bind(view)
        val adapter = ProductSummaryAdapter(this)
        productRecycleViewModel.getProductSummary(
            requireActivity().getSharedPreferences
                (
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            ).getString("userId", "0")!!.toLong()
        )
        setObserve(adapter)
        setUpRecycleView(adapter)
        callback = parentFragment as RecycleProductViewCallback
    }

    private fun setObserve(adapter: ProductSummaryAdapter) {

        productRecycleViewModel.data.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        productRecycleViewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                binding.productRecycleView.adapter = adapter
            }
        }

    }

    private fun setUpRecycleView(adapter: ProductSummaryAdapter) {
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
        recyclerView.adapter = adapter

    }

    override fun callbackOnClick(productId: Long) {
        callback.productItemIsSelected(productId)
    }
}