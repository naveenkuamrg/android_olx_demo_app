package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.model.ProductSortType

class ProductSortTypeViewModel: ViewModel() {
    private val _currentSortType: MutableLiveData<ProductSortType> =
        MutableLiveData(ProductSortType.POSTED_DATE_DESC)
    var currentSortType: LiveData<ProductSortType> = _currentSortType

    fun setCurrentProductType(type: ProductSortType) {
        _currentSortType.value = type
    }

    companion object{
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>,): T {
                return ProductSortTypeViewModel() as T
            }
        }
    }
}