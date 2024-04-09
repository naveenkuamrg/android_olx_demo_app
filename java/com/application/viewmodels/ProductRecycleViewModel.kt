package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductSortType
import com.application.model.ProductListItem.ProductItem
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRecycleViewModel(private val productRepository: ProductRepository) : ViewModel() {


    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _data: MutableLiveData<List<ProductItem>> = MutableLiveData()

    val data: LiveData<List<ProductItem>> = _data

    fun getPostProductSummary(id: Long) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getProductSummaryDetailsForSellZone(id))
            _isLoading.postValue(false)
        }
    }

    fun getBuyProductSummary(id: Long, sortType: ProductSortType) {
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.postValue(true)
            _data.postValue(
                productRepository.getProductSummaryDetailsForBuyZone(
                    id,
                    sortType
                )
            )
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


class ProductRecycleViewModelCheck : ViewModel() {

     val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)


    private val _data: MutableLiveData<List<ProductItem>> = MutableLiveData()
    val data: LiveData<List<ProductItem>> = _data

    fun setData(list: List<ProductItem>){
        _data.postValue(list)
    }

    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {



                return ProductRecycleViewModelCheck() as T
            }
        }
    }
}