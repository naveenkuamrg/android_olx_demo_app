package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.fragments.YoursActivityFragment
import com.application.model.ProductListItem
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YoursActivityPageViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _data: MutableLiveData<List<ProductListItem.ProductItem>> = MutableLiveData()
    val data: LiveData<List<ProductListItem.ProductItem>> = _data


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

                return YoursActivityPageViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}