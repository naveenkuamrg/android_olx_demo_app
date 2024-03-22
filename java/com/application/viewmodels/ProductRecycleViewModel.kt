package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductSummary
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRecycleViewModel(val productRepository: ProductRepository) : ViewModel() {

    private val _data: MutableLiveData<List<ProductSummary>> = MutableLiveData(
        mutableListOf()
    )

    val data: LiveData<List<ProductSummary>> = _data

    fun getProductSummary(id: Long){
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getProductSummaryDetailsForSellZone(id))
        }
    }

    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]


                return ProductRecycleViewModel(
                    ProductRepositoryImpl
                        (application!!.applicationContext)
                ) as T
            }
        }
    }
}