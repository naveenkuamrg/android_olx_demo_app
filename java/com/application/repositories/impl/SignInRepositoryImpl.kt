package com.application.repositories.impl

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import com.application.AppDatabase
import com.application.entity.User
import com.application.model.AuthenticationResult
import com.application.repositories.SignInRepository

class SignInRepositoryImpl(context : Context) : SignInRepository {
    private val userDao = AppDatabase.getInstance(context).userDao


    override suspend fun getUserId(email: String, password: String): Long {
        return  userDao.getUserId(email)
    }

    override suspend fun isEmailExist(email: String): Boolean {
       return userDao.isEmailExist(email)
    }

    override suspend fun isPasswordMatch(email: String, password: String): Boolean {
       return userDao.isPasswordMatch(email,password)
    }




}