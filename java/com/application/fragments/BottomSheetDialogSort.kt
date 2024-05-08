package com.application.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import com.application.R
import com.application.callbacks.SortBottomSheetCallback
import com.application.databinding.FragmentBottomSheetSortProductBinding
import com.application.model.ProductSortType
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomSheetDialogSort :
    BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_sort_product) {
    private lateinit var binding: FragmentBottomSheetSortProductBinding
    private lateinit var callback: SortBottomSheetCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetSortProductBinding.bind(view)
//        val behavior = BottomSheetBehavior.from(view)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setOnClickListener()
        callback = parentFragment as SortBottomSheetCallback
        when (callback.getCurrentSortType()) {
            ProductSortType.POSTED_DATE_DESC -> {
                binding.postedDateHigherToLower.isChecked = true
            }

            ProductSortType.POSTED_DATE_ASC -> {
                binding.postedDateLowerToHigher.isChecked = true
            }

            ProductSortType.PRICE_ASC -> {
                binding.priceLowerToHigher.isChecked = true
            }

            ProductSortType.PRICE_DESC -> {
                binding.priceDateLowerToHigher.isChecked = true
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun setOnClickListener() {
        binding.postedDateLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.POSTED_DATE_ASC)
//            (it as Chip).isChecked = true
        }
        binding.postedDateHigherToLower.setOnClickListener {
            onClickListener(ProductSortType.POSTED_DATE_DESC)
//            (it as Chip).isChecked = true
        }
        binding.priceLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.PRICE_ASC)
//            (it as Chip).isChecked = true
        }
        binding.priceDateLowerToHigher.setOnClickListener {
            onClickListener(ProductSortType.PRICE_DESC)
//            (it as Chip).isChecked = true
        }




    }

    private fun onClickListener(sortType: ProductSortType) {

        callback.onSortTypeSelected(sortType)
        this.dismiss()
    }

}