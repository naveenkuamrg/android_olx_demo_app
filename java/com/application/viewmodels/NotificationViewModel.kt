package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.model.Notification
import com.application.repositories.NotificationRepository
import com.application.repositories.impl.NotificationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationRepository: NotificationRepository) :
    ViewModel() {


    val notifications: LiveData<PagingData<Notification>> =
        notificationRepository.getNotification().cachedIn(viewModelScope)

    private val _isUnReadNotification: MutableLiveData<Boolean> = MutableLiveData()
    val isUnReadNotification: LiveData<Boolean> = _isUnReadNotification



    fun getIsReadNotification() {
        viewModelScope.launch(Dispatchers.Default) {
            _isUnReadNotification.postValue(notificationRepository.isUnreadNotification())
        }
    }

    fun updateNotificationIsReadStatus(notificationId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            notificationRepository.updateNotificationIsReadStatus(notificationId)
        }

    }

    fun updateAllNotificationIsRead(){
        viewModelScope.launch(Dispatchers.Default) {
            notificationRepository.updateAllNotificationIsReadStatus()
        }
    }



    companion object {
        val FACTORY = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]


                return NotificationViewModel(
                    NotificationRepositoryImpl(application!!.applicationContext)
                ) as T
            }
        }
    }
}