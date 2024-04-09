package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentSortType: MutableLiveData<ProductSortType> = MutableLiveData()
    var currentSortType: LiveData<ProductSortType> = _currentSortType

    private val _data: MutableLiveData<List<ProductListItem.ProductItem>> = MutableLiveData()
    val data: LiveData<List<ProductListItem.ProductItem>> = _data

    fun getProductSummary(id: Long) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getProductSummaryDetailsForSellZone(id))
            _isLoading.postValue(false)
        }
    }

    fun getProductSummary(id: Long, sortType: ProductSortType) {
        _currentSortType.value = sortType
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


    fun setCurrentProductType(type: ProductSortType) {
        _currentSortType.value = type
    }

    fun getProductSummary(id: Long, sortType: ProductSortType,type: ProductType) {
        _currentSortType.value = sortType
        viewModelScope.launch(Dispatchers.Default) {
            _isLoading.postValue(true)
            _data.postValue(
                productRepository.getProductSummaryDetailsForBuyZone(
                    id,
                    type,
                    sortType
                )
            )
            _isLoading.postValue(false)
        }
    }

    fun getFavoriteProductSummary(userId: Long){
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getFavouriteProductList(userId))
        }
    }

    fun getInterestedProductSummary(userId: Long){
        viewModelScope.launch(Dispatchers.Default) {
            _data.postValue(productRepository.getInterestedProductList(userId))
        }
    }




    companion object {
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                return ProductListViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}