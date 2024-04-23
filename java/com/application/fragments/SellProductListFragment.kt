package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.ProductViewCallback
import com.application.databinding.FragmentSellZoneBinding
import com.google.android.material.progressindicator.CircularProgressIndicator

class SellProductListFragment : BaseProductListFragment(R.layout.fragment_sell_zone){
    lateinit var binding: FragmentSellZoneBinding


    private lateinit var callBack: ProductViewCallback

    override lateinit var recyclerView: RecyclerView

    override lateinit var progressIndicator: CircularProgressIndicator



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSellZoneBinding.bind(view)
        recyclerView = binding.recycleView
        progressIndicator = binding.progressCircular
        super.onViewCreated(view, savedInstanceState)
        callBack = parentFragment as ProductViewCallback
        binding.noData.errorText.text = "You din't Post any Product"
        setOnClickListenerAddProduct()
        setObserve()

    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.onShowProductEditDetailPage()
        }
    }

    override fun onProductSummaryClick(productId: Long) {
        callBack.onShowProductDetailsPage(productId)
    }

    override fun isListEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.noData.noDataLayout.visibility = View.VISIBLE
        } else {
            binding.noData.noDataLayout.visibility = View.GONE
        }
    }

    private fun setObserve() {
        productListViewModel.sellProductList.observe(viewLifecycleOwner) {
            setData(it)
        }
    }
}