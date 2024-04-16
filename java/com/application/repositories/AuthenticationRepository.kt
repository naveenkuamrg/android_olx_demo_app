package com.application.repositories

import com.application.model.Profile

interface AuthenticationRepository {

    suspend fun setUserProfile(name: String,
                               email: String,
                               phoneNumber: String,
                               password: String): Long

    suspend fun getUser(identifier: String, password: String) : Profile

}