package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.Profile
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean?> = MutableLiveData()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val _product: MutableLiveData<Product?> = MutableLiveData()
    val product: LiveData<Product?> = _product

    private val _isDelete: MutableLiveData<Boolean?> = MutableLiveData()
    val isDelete: LiveData<Boolean?> = _isDelete

    private val _isInterestedChangeIsUpdate: MutableLiveData<Boolean?> = MutableLiveData()
    val isInterestedChangeIsUpdate: LiveData<Boolean?> = _isInterestedChangeIsUpdate

    private val _profileList: MutableLiveData<List<Profile>> = MutableLiveData()
    val profileList: LiveData<List<Profile>> = _profileList


    fun fetchProductDetails(productId: Long, userId: Long) {
        if(productId != -1L) {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.Default) {
                _product.postValue(productRepository.getProductDetails(productId, userId))
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchProfileList(productId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            _profileList.postValue(productRepository.getInterestedProfile(productId))
        }
    }

    fun updateProductInterested(productId: Long, userId: Long, isInterested: Boolean) {
        _isInterestedChangeIsUpdate.value = null
        viewModelScope.launch(Dispatchers.Default) {
            _isInterestedChangeIsUpdate.postValue(
                productRepository.updateProductIsInterested(
                    userId,
                    productId,
                    isInterested
                )
            )
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

    fun clearLiveData() {
        _isDelete.value = null
        _isLoading.value = null
        _isInterestedChangeIsUpdate.value = null
    }

    fun updateMarkAsSold() {
        viewModelScope.launch(Dispatchers.Default) {
            product.value?.let {
                productRepository.updateProductAvailabilityStatus(
                    it,
                    AvailabilityStatus.SOLD_OUT
                )
            }
            _isLoading.postValue(true)
            _product.postValue(
                productRepository.getProductDetails(
                    product.value?.id!!,
                    product.value?.sellerId!!
                )
            )
            _isLoading.postValue(false)
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