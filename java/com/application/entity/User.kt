package com.application.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "user",indices = [Index(value = ["email"], unique = true)])
data class User(
    val name : String,
    val phoneNumber: String,
    val email: String,
    val password : String,
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id : Long = 0
}





