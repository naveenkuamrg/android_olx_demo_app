package com.application.repositories

import com.application.model.Profile

interface UserRepository {
    fun getUserProfile(userId : Long): Profile

    fun updateUserName(name: String,userId: Long )

    fun updateUserEmail(email: String,userId: Long)

    fun updateUserPhone(phone: String,userId: Long)
}