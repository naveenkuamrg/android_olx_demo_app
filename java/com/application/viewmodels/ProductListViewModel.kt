package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import com.application.model.ProductListItem
import com.application.model.ProductType
import com.application.repositories.ProductRepository
import com.application.repositories.impl.ProductRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductListViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val sellProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getProductSummaryDetailsForSellZone()
            .asLiveData()
            .cachedIn(CoroutineScope(Dispatchers.IO))
    val favoriteProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getFavouriteProductList()
            .asLiveData()
            .cachedIn(CoroutineScope(Dispatchers.IO))

    val interestedProductList: LiveData<PagingData<ProductListItem>> =
        productRepository.getInterestedProductList()
            .asLiveData()
            .cachedIn(CoroutineScope(Dispatchers.IO))

     var productListPostedDateASC: LiveData<PagingData<ProductListItem>> = setHeader(
         productRepository.getProductSummaryDetailsForBuyZonePostedDateASC()
     )
     var productListPostedDateDESC: LiveData<PagingData<ProductListItem>> =  setHeader(
         productRepository.getProductSummaryDetailsForBuyZonePostedDateDESC()
     )

    var productListPricesASC: LiveData<PagingData<ProductListItem>> = setHeader(
        productRepository.getProductSummaryDetailsForBuyZonePriceASC()
    )

    var productListPricesDESC: LiveData<PagingData<ProductListItem>> = setHeader(
        productRepository.getProductSummaryDetailsForBuyZonePriceDESC()
    )



    private fun setHeader(pagingData: Flow<PagingData<ProductListItem>>):
            LiveData<PagingData<ProductListItem>> {
        return pagingData.map {
            it.insertHeaderItem(TerminalSeparatorType.FULLY_COMPLETE, ProductListItem.Header())
        }.asLiveData().cachedIn(CoroutineScope(Dispatchers.IO))
    }

    fun getProductSummary(type: ProductType) {
        productListPostedDateASC =
            productRepository.getProductSummaryDetailsForBuyZonePostedDateASC(type)
                .asLiveData()
                .cachedIn(CoroutineScope(Dispatchers.IO))
        productListPostedDateDESC =
            productRepository.getProductSummaryDetailsForBuyZonePostedDateDESC(type)
                .asLiveData()
                .cachedIn(CoroutineScope(Dispatchers.IO))
        productListPricesASC =
            productRepository.getProductSummaryDetailsForBuyZonePriceASC(type)
                .asLiveData()
                .cachedIn(CoroutineScope(Dispatchers.IO))
        productListPricesDESC =
            productRepository.getProductSummaryDetailsForBuyZonePriceDESC(type)
                .asLiveData()
                .cachedIn(CoroutineScope(Dispatchers.IO))
    }


    companion object {
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                return ProductListViewModel(
                    ProductRepositoryImpl(application!!.applicationContext)) as T
            }
        }
    }
}