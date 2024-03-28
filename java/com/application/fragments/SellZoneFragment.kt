package com.application.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.RecycleProductViewCallback
import com.application.callbacks.SellZoneFragmentCallback
import com.application.databinding.FragmentSellZoneBinding
import com.application.viewmodels.SellZoneViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone), RecycleProductViewCallback {
    lateinit var binding: FragmentSellZoneBinding

    val viewModel: SellZoneViewModel by viewModels { SellZoneViewModel.FACTORY }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container_view_tag, ProductRecycleViewFragment())
                commit()
            }
        }
    }

    lateinit var callBack: SellZoneFragmentCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellZoneBinding.bind(view)
        callBack = parentFragment as SellZoneFragmentCallback
        setOnClickListenerAddProduct()

    }

    private fun setOnClickListenerAddProduct() {
        binding.addProduct.setOnClickListener {
            callBack.showProductEditDetailPage()
        }
    }

    override fun productItemIsSelected(productId: Long) {
        callBack.showProductDetailsPage(productId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG SellZoneFragment","onDestroy")
    }
}