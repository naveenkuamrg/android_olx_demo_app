package com.application.repositories

import com.application.entity.User
import com.application.model.AuthenticationResult

interface SignInRepository {
    suspend fun getUserId(email : String, password : String) : Long

    suspend fun isEmailExist(email : String) : Boolean

    suspend fun isPasswordMatch(email: String,password: String) : Boolean
}