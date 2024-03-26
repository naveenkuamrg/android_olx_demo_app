package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _product: MutableLiveData<Product?> = MutableLiveData()
    val product: LiveData<Product?> = _product

    private val _isDelete: MutableLiveData<Boolean?> = MutableLiveData()
    val isDelete: LiveData<Boolean?> = _isDelete


    fun fetchProductDetails(productId: Long, userId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _product.postValue(productRepository.getProductDetails(productId, userId))
            _isLoading.postValue(true)
        }

    }


    fun removeProductDetail() {
        viewModelScope.launch(Dispatchers.Default) {
            product.value?.let {
                _isDelete.postValue(productRepository.removeProduct(it))
            }
        }
    }

    fun clearProduct() {
        _product.value = null
    }

    fun clearIsDelete() {
        _isDelete.value = null
    }

    fun updateMarkAsSold() {
        viewModelScope.launch(Dispatchers.Default) {
            product.value?.let {
                productRepository.updateProductAvailabilityStatus(
                    it,
                    AvailabilityStatus.SOLD_OUT
                )
            }
            _isLoading.postValue(false)
            _product.postValue(
                productRepository.getProductDetails(
                    product.value?.id!!,
                    product.value?.sellerId!!
                )
            )
            _isLoading.postValue(true)

        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]



                return ProductDetailViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}