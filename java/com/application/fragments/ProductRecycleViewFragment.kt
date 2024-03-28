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
        val userId = requireActivity().getSharedPreferences(
            "mySharePref", AppCompatActivity.MODE_PRIVATE
        ).getString("userId","-1")!!.toLong()
        binding = FragmentProductRecycleViewBinding.bind(view)
        val adapter = ProductSummaryAdapter(this)
        Log.i("TAG parentFragment ", parentFragment.toString())
        Log.i("TAG ProductRecycleViewFragment",savedInstanceState.toString())
        if(productRecycleViewModel.isLoading.value != false) {
            when (parentFragment) {
                is SellZoneFragment -> {
                    productRecycleViewModel.getPostProductSummary(userId)
                }

                is HomeFragment -> {
                    productRecycleViewModel.getBuyProductSummary(userId)
                }
            }
        }

        setObserve(adapter,savedInstanceState)
        setUpRecycleView(adapter)
        Log.i("TAG class", (parentFragment is RecycleProductViewCallback).toString())
        callback = parentFragment as RecycleProductViewCallback
    }

    private fun setObserve(adapter: ProductSummaryAdapter,savedInstanceState: Bundle?) {

        productRecycleViewModel.data.observe(viewLifecycleOwner) {
            Log.i("TAG ProductRecycleViewFragment","RELOAD set ${savedInstanceState == null}")

                adapter.data = it
                binding.productRecycleView.adapter = adapter
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

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG ProductRecycleViewFragment","onDestroy  ${parentFragment.toString()}")
    }
}