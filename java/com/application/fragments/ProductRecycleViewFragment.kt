package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.R
import com.application.adapter.ProductSummaryAdapter
import com.application.adapter.ProductSummaryAdapterWithFilter
import com.application.callbacks.OnFilterItemClickListener
import com.application.callbacks.OnItemClickListener
import com.application.callbacks.ProductRecycleViewModelCallback
import com.application.databinding.FragmentProductRecycleViewBinding
import com.application.model.ProductListItem.ProductItem
import com.application.model.ProductType
class ProductRecycleViewFragment : Fragment(R.layout.fragment_product_recycle_view),
    OnFilterItemClickListener, ProductRecycleViewModelCallback {

    lateinit var callback: OnItemClickListener
    lateinit var binding: FragmentProductRecycleViewBinding
    lateinit var adapter: ProductSummaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductRecycleViewBinding.bind(view)
        binding.productRecycleView.itemAnimator = null
        Log.i("TAG",parentFragment.toString())
        adapter = if(parentFragment is OnFilterItemClickListener){
            ProductSummaryAdapterWithFilter(this)
        }else{
            ProductSummaryAdapter(this)
        }

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
        recyclerView.adapter = adapter

    }

    override fun onFilterItemClick(productType: ProductType) {
        (callback as OnFilterItemClickListener).onFilterItemClick(productType)
    }

    override fun onItemClick(position: Int) {
        callback.onItemClick(position)
    }

    override fun onSetData(list: List<ProductItem>) {
        adapter.submitData(list)
        binding.progressCircular.visibility = View.GONE
    }

}