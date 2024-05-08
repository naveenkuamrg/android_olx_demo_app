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
    var isDataUpdate: Boolean = false

    private var _isUpload: MutableLiveData<Boolean?> = MutableLiveData()
    val isUpload: LiveData<Boolean?> = _isUpload

    private var _product: MutableLiveData<Product?> = MutableLiveData()
    val product: LiveData<Product?> = _product


    private val _images: MutableLiveData<MutableList<Bitmap>?> = MutableLiveData(mutableListOf())
    val images: LiveData<MutableList<Bitmap>?> = _images


    fun postProduct(
        id: Long?,
        title: String,
        description: String,
        data: Long,
        price: Double,
        category: String,
        location: String,
        userId: Long
    ): Boolean {
        viewModelScope.launch(Dispatchers.Default) {
            _isUpload.postValue(
                productRepository.insertProduct(
                    Product(
                        id,
                        title,
                        price,
                        data,
                        description,
                        AvailabilityStatus.AVAILABLE,
                        location,
                        ProductType.stringToProductType(category)!!,
                        userId,
                    ).apply {
                        images = this@EditProductViewModel.images.value!!
                    }
                ) != 0L
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
    }

    fun setProduct(product: Product){
        Log.i("TAG naveen",product.toString())
        _product.value = product
    }

    fun setTempImages(images: List<Bitmap>){
        _images.value = images.toMutableList()
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