package com.application.repositories.impl

import android.content.Context
import android.net.Uri
import com.application.AppDatabase
import com.application.helper.ModelConverter
import com.application.model.Profile
import com.application.repositories.UserRepository

class UserRepositoryImpl(context : Context) : UserRepository {
    val dao = AppDatabase.getInstance(context).userDao
    override fun getUserProfile(userId: Long): Profile {
        return ModelConverter.profileFromUserAndUri(dao.getUser(userId), Uri.parse(""))
    }

    override fun updateUserName(name: String,userId: Long) {
        dao.updateUserName(name,userId)
    }

    override fun updateUserEmail(email: String,userId: Long) {
        dao.updateUserEmail(email,userId)
    }

    override fun updateUserPhone(phone: String,userId: Long) {
       dao.updateUserPhone(phone,userId)
    }

}