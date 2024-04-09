package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductListItem.ProductItem


class ProductRecycleViewModel : ViewModel() {


    private val _data: MutableLiveData<List<ProductItem>> = MutableLiveData()
    val data: LiveData<List<ProductItem>> = _data

    fun setData(list: List<ProductItem>){
        _data.postValue(list)
    }

    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {



                return ProductRecycleViewModel() as T
            }
        }
    }
}