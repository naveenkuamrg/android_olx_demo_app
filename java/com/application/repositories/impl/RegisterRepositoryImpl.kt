package com.application.repositories.impl

import android.content.Context
import com.application.AppDatabase
import com.application.dao.UserDao
import com.application.helper.ModelConverter
import com.application.model.AuthenticationResult
import com.application.model.RegisterResult
import com.application.repositories.RegisterRepository

class RegisterRepositoryImpl(context : Context) : RegisterRepository {

    private val userDao : UserDao = AppDatabase.getInstance(context).userDao
    override suspend fun setUserProfile(
        name: String,
        email: String,
        phoneNumber: String,
        password: String
    ) {
        userDao.insertUser(
            ModelConverter.UserEntityFromUserDetails(name, phoneNumber, email, password)
        )
    }

    override suspend fun isEmailExist(email: String): Boolean {
        return userDao.isEmailExist(email)
    }
}