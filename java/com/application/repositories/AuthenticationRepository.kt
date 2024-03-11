package com.application.repositories

interface AuthenticationRepository {

    suspend fun setUserProfile(name: String,
                               email: String,
                               phoneNumber: String,
                               password: String): Long

    suspend fun getUserId(email: String, password: String) : Long

}