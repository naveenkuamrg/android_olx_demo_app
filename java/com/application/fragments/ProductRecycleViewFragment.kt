package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.callbacks.ProductAdapterCallback
import com.application.callbacks.RecycleProductViewCallback
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.viewmodels.ProductRecycleViewModel

class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view),
    ProductAdapterCallback {

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
            when (parentFragment) {
                is SellZoneFragment -> {
                    productRecycleViewModel.getPostProductSummary(userId)
                }
                is HomeFragment -> {
                    productRecycleViewModel.getBuyProductSummary(userId)
                }
            }
        setObserve()
        setUpRecycleView()
        callback = parentFragment as RecycleProductViewCallback
    }

    private fun setObserve() {
        productRecycleViewModel.data.observe(viewLifecycleOwner) {
            (binding.productRecycleView.adapter as ProductSummaryAdapter).saveData(it)
        }
        productRecycleViewModel.isLoading.observe(viewLifecycleOwner) {
            //setLoader
            if (!it) {
                binding.progressCircular.visibility = View.GONE
            }
        }

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
        recyclerView.adapter = ProductSummaryAdapter(this)

    }

    override fun callbackOnClick(productId: Long) {
        callback.productItemIsSelected(productId)
    }

}