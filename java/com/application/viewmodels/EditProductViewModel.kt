package com.application.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductType
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class EditProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _isUpload: MutableLiveData<Boolean> = MutableLiveData()
    val isUpload: LiveData<Boolean> = _isUpload

    private val _product: MutableLiveData<Product> = MutableLiveData()
    val product: LiveData<Product> = _product


    val _images: MutableLiveData<List<Bitmap>> = MutableLiveData(listOf())
    val images: LiveData<List<Bitmap>> = _images


    fun postProduct(
        title: String,
        description: String,
        price: Double,
        category: String,
        location: String,
        userId: Long
    ): Boolean {
        viewModelScope.launch(Dispatchers.Default) {
            _isUpload.postValue(
                productRepository.insertProductRepository(
                    Product(
                        null,
                        title,
                        price,
                        Date(),
                        images.value!!,
                        description,
                        AvailabilityStatus.AVAILABLE,
                        location,
                        ProductType.FASHION,
                        userId,
                        isInterested = false,
                        isWishList = false
                    )
                )
            )

        }
        return true
    }

    fun updateImages(addedImage: Bitmap){
        val result: MutableList<Bitmap> = mutableListOf()
        images.value?.map {
            result.add(it)
        }
        result.add(addedImage)
        _images.postValue(result)
    }


    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                return EditProductViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }

}