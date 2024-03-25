package com.application.viewmodels

import android.graphics.Bitmap
import android.util.Log
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


    private val _images: MutableLiveData<MutableList<Bitmap>> = MutableLiveData(mutableListOf())
    val images: LiveData<MutableList<Bitmap>> = _images


    fun postProduct(
        title: String,
        description: String,
        price: Double,
        category: String,
        location: String,
        userId: Long
    ): Boolean {
        Log.i("TAG images",images.value.toString())
        viewModelScope.launch(Dispatchers.Default) {
            Log.i("tag productId",product.value?.id.toString())
            _isUpload.postValue(
                productRepository.insertProduct(
                    Product(
                        product.value?.id,
                        title,
                        price,
                        Date().time,
                        description,
                        AvailabilityStatus.AVAILABLE,
                        location,
                        ProductType.stringToProductType(category)!!,
                        userId,
                    ).apply {
                        images = this@EditProductViewModel.images.value!!
                    }
                )
            )

        }
        return true
    }

    fun updateImage(addedImage: Bitmap) {
        _images.value = images.value?.apply {
            add(addedImage)
        }
    }


    fun removeImage(position: Int) {
        _images.value = images.value?.apply {
            removeAt(position)
        }
        Log.i("TAG", _images.value?.size.toString())
    }

    fun setProduct(product: Product){
        _product.value = product
        _images.value = product.images.toMutableList()
    }

    fun clearImageList() {
        _images.value = mutableListOf()
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