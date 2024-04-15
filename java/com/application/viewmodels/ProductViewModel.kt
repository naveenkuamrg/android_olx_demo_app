package com.application.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.exceptions.ProductDataException
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.Profile
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

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

    private val _isWishListIsUpdate: MutableLiveData<Boolean?> = MutableLiveData()
    val isWishListIsUpdate: LiveData<Boolean?> = _isWishListIsUpdate

    private val _exception: MutableLiveData<Exception?> = MutableLiveData()
    val exception: MutableLiveData<Exception?> = _exception


    fun fetchProductDetailsUsingProductId(productId: Long, userId: Long) {
        if (productId != -1L) {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.Default) {
                _product.postValue(
                    productRepository.getProductDetailsUsingProductId(
                        productId,
                        userId
                    )
                )
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchProductDetailsUsingNotificationId(notificationId: Long, userId: Long) {
        if (notificationId != -1L) {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.Default) {
                try {
                    val product = productRepository.getProductDetailsUsingNotificationId(
                        notificationId,
                        userId
                    )
                    _product.postValue(
                        product
                    )
                } catch (e: ProductDataException) {
                    withContext(Dispatchers.Main) {
                        _exception.value = e
                    }
                } finally {
                    _isLoading.postValue(false)
                }

            }
        }

    }

    fun fetchProfileList(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _profileList.postValue(productRepository.getInterestedProfile(productId))
        }
    }

    fun updateProductInterested(product: Product, userId: Long, isInterested: Boolean) {
        _isInterestedChangeIsUpdate.value = null
        viewModelScope.launch(Dispatchers.Default) {
            _isInterestedChangeIsUpdate.postValue(
                productRepository.updateProductIsInterested(
                    userId,
                    product,
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
        _exception.value = null
        _profileList.value = mutableListOf()
    }

    fun updateMarkAsSold() {
        viewModelScope.launch(Dispatchers.Default) {
            product.value?.let {
                productRepository.updateProductAvailabilityAndNotify(
                    it,
                    AvailabilityStatus.SOLD_OUT,
                    profileList.value!!
                )
            }
            _isLoading.postValue(true)
            _product.postValue(
                productRepository.getProductDetailsUsingProductId(
                    product.value?.id!!,
                    product.value?.sellerId!!
                )
            )
            _isLoading.postValue(false)
        }
    }

    fun updateIsInterested(userId: Long) {
        _isWishListIsUpdate.value = false
        product.value?.isWishList = !product.value?.isWishList!!
        viewModelScope.launch(Dispatchers.Default) {
            productRepository.updateIsFavorite(product.value!!, product.value?.isWishList!!, userId)
            _isWishListIsUpdate.postValue(true)
        }

    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]



                return ProductViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}