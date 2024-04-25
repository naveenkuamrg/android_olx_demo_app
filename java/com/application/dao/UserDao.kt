package com.application.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.application.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user : User): Long
    @Query("select user_id from user where email LIKE :email")
    fun getUserId(email: String) : Long

    @Query("select * from user where email LIKE :identifier OR phoneNumber LIKE :identifier" )
    fun getUser(identifier: String) : User?

    //UserDao
    @Query("select * from user where user_id LIKE :userId")
    fun getUser(userId : Long): User

    @Query("UPDATE user SET name = :name where user_id LIKE :id ")
    fun updateUserName(name: String,id: Long): Int
    @Query("UPDATE user SET email = :email where user_id LIKE :id ")
    fun updateUserEmail(email: String,id: Long): Int
    @Query("UPDATE user SET phoneNumber = :phoneNumber where user_id LIKE :id ")
    fun updateUserPhone(phoneNumber: String,id: Long): Int

    @Query("UPDATE user SET password = :newPassword where user_id Like :id and password LIKE :currentPassword")
    fun updateUserPassword(id: Long,currentPassword: String,newPassword: String) : Int

}
