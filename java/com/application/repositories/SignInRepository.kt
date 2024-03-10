package com.application.repositories

import com.application.entity.User

interface SignInRepository {

    suspend fun getUsers() : List<User>

    suspend fun getPassword(email : String) : String
    suspend fun getUserId(email : String, password : String) : Int
}