package com.application.fragments

import android.os.Bundle
import android.view.View
import com.application.R
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentBottomSheetSortProductBinding
import com.application.model.ProductSortType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogSort :
    BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_sort_product) {
        lateinit var binding: FragmentBottomSheetSortProductBinding
        lateinit var callback: SortBottomSheetCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetSortProductBinding.bind(view)
        setOnClickListener()
        callback = parentFragment as SortBottomSheetCallback
    }


    private fun setOnClickListener(){
        binding.postedDateLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.POSTED_DATE_ASC)
        }
        binding.postedDateHigherToLower.setOnClickListener {
            onClickListener(ProductSortType.POSTED_DATE_DESC)

        }
        binding.priceLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.PRICE_ASC)

        }
        binding.priceDateLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.PRICE_DESC)

        }
    }

    private fun onClickListener(sortType: ProductSortType){
        callback.onSortTypeSelected(sortType)
        this.dismiss()
    }

}