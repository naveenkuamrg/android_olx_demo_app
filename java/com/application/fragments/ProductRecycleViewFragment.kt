package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.model.ProductSummary
import com.application.viewmodels.ProductRecycleViewModel

class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view) {

    val productRecycleViewModel: ProductRecycleViewModel by viewModels {
        ProductRecycleViewModel.FACTORY
    }

    lateinit var binding: FragmentProductRecycleViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductRecycleViewBinding.bind(view)
        val adapter = ProductSummaryAdapter()
        setObserve(adapter)
        setUpRecycleView(adapter)



        productRecycleViewModel.getProductSummary(
            requireActivity().getSharedPreferences
                (
                "mySharePref",
                AppCompatActivity.MODE_PRIVATE
            ).getString("userId", "0")!!.toLong()
        )
    }

    private fun setObserve(adapter: ProductSummaryAdapter) {

        productRecycleViewModel.data.observe(viewLifecycleOwner) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

    }

    private fun setUpRecycleView(adapter: ProductSummaryAdapter){
        binding.productRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.productRecycleView.adapter = adapter
    }
}