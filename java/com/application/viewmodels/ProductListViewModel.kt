package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.model.ProductListItem
import com.application.model.ProductSortType
import com.application.model.ProductType
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.Dispatchers

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {


    private val _currentSortType: MutableLiveData<ProductSortType> =
        MutableLiveData(ProductSortType.POSTED_DATE_DESC)
    var currentSortType: LiveData<ProductSortType> = _currentSortType


    val sellProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForSellZone().asLiveData(Dispatchers.IO)
            .cachedIn(viewModelScope)
    val favoriteProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getFavouriteProductList().asLiveData(Dispatchers.IO)

    val interestedProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getInterestedProductList().asLiveData(Dispatchers.IO)

    var productListPostedDateASC: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForBuyZonePostedDateASC()
            .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)

     var productListPostedDateDESC: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForBuyZonePostedDateDESC()
            .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)

     var productListPricesASC: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForBuyZonePriceASC()
            .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)

     var productListPricesDESC: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForBuyZonePriceDESC()
            .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)

    fun setCurrentProductType(type: ProductSortType) {
        _currentSortType.value = type
    }

    fun getProductSummary(type: ProductType) {
        productListPostedDateASC =
            productRepository.getProductSummaryDetailsForBuyZonePostedDateASC(type)
                .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
        productListPostedDateDESC =
            productRepository.getProductSummaryDetailsForBuyZonePostedDateDESC(type)
                .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
        productListPricesASC =
            productRepository.getProductSummaryDetailsForBuyZonePriceASC(type)
                .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
        productListPricesDESC =
            productRepository.getProductSummaryDetailsForBuyZonePriceDESC(type)
                .asLiveData(Dispatchers.IO).cachedIn(viewModelScope)
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