package com.application.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.application.R
import com.application.callbacks.SellZoneFragmentCallBack
import com.application.databinding.FragmentSellZoneBinding
import com.application.viewmodels.SellZoneViewModel

class SellZoneFragment : Fragment(R.layout.fragment_sell_zone) {
    lateinit var binding: FragmentSellZoneBinding

    val viewModel : SellZoneViewModel by viewModels {SellZoneViewModel.FACTORY }

    lateinit var callBack : SellZoneFragmentCallBack
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellZoneBinding.bind(view)
        callBack = parentFragment as SellZoneFragmentCallBack
        setOnClickListenerAddProduct()
    }

    private fun setOnClickListenerAddProduct(){
        binding.addProduct.setOnClickListener {
            callBack.showEditDetailPage()
        }
    }
}