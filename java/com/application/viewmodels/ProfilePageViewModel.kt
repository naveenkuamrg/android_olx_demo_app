package com.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.application.model.Profile
import com.application.repositories.UserRepository
import com.application.repositories.impl.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePageViewModel(val userRepository : UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile : LiveData<Profile> = _profile

    fun showProfile(userId : Long){
        viewModelScope.launch(Dispatchers.Default) {
            _profile.postValue(userRepository.getUserProfile(userId))
        }
    }
    companion object {
        val FACTORY = object  :  ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                val repositoryImpl = UserRepositoryImpl(application!!.applicationContext)

                return ProfilePageViewModel(repositoryImpl) as T
            }
        }
    }

}