package com.application.viewmodels

import android.util.Log
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

class ProductRecycleViewModel(private val productRepository: ProductRepository) : ViewModel() {

    init {
        Log.i("TAG ProductRecycleViewModel","init")
    }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _data: MutableLiveData<List<ProductSummary>> = MutableLiveData()

    val data: LiveData<List<ProductSummary>> = _data

    fun getPostProductSummary(id: Long) {

        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getProductSummaryDetailsForSellZone(id))
            _isLoading.postValue(false)
        }
    }

    fun getBuyProductSummary(id: Long) {
        Log.i("TAG ProductRecycleViewModel","getBuyProductSummary")
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getProductSummaryDetailsForBuyZone(id))
            _isLoading.postValue(false)
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