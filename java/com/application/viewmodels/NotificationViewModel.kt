package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.Notification
import com.application.repositories.NotificationRepository
import com.application.repositories.impl.NotificationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationRepository: NotificationRepository) :
    ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val notifications: LiveData<List<Notification>> = _notifications

    fun fetchNotification(userId: Long) {
        _isLoading.value = false
        viewModelScope.launch(Dispatchers.Default) {
            _notifications.postValue(notificationRepository.getNotification(userId))
            _isLoading.postValue(true)
        }
    }

    fun updateNotificationIsReadStatus(userId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            notificationRepository.updateNotificationIsReadStatus(userId)
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