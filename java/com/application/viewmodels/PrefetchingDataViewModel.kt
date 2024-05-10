package com.application.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class PrefetchingDataViewModel: ViewModel() {

    val loading: MutableLiveData<Boolean> = MutableLiveData()



    companion object{
        val FACTORY = object : ViewModelProvider.Factory{

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return PrefetchingDataViewModel() as T
            }
        }
    }
}