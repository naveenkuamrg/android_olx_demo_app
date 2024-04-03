package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductSortType

class HomeViewModel : ViewModel() {
    private val _currentSortType: MutableLiveData<ProductSortType> =
        MutableLiveData(ProductSortType.POSTED_DATE_ASC)
    var currentSortType: LiveData<ProductSortType> = _currentSortType


    fun setSortType(type: ProductSortType){
        _currentSortType.value = type
    }


    companion object {

        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                return HomeViewModel() as T
            }
        }
    }
}