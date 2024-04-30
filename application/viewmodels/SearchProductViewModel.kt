package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.SearchProductResultItem
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _searchResult: MutableLiveData<List<SearchProductResultItem>> = MutableLiveData()
    val searchResult: LiveData<List<SearchProductResultItem>> = _searchResult
    fun search(searchTerm: String,userId: Long){
        if(searchTerm != "") {
            viewModelScope.launch(Dispatchers.Default) {
                _searchResult.postValue(productRepository.getSearchProduct(searchTerm, userId))
            }
        }else{
            _searchResult.postValue(mutableListOf())
        }
    }


    companion object {
        val FACTORY = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                return SearchProductViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}