package com.application.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductSortType

class HomeViewModel: ViewModel() {
    val currentSortType: ProductSortType = ProductSortType.PRICE_ASC



    companion object{

        @Suppress("UNCHECKED_CAST")
        val FACTORY = object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                return HomeViewModel() as T
            }
        }
    }
}