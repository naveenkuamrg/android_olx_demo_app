package com.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.dao.ProductDao
import com.application.dao.UserDao
import com.application.entity.InterestedList
import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList

@Database(entities = [User::class,ProductDetails::class,WishList::class,InterestedList::class,
    Notification :: class]
    , version = 1)
abstract  class AppDatabase : RoomDatabase() {

    abstract  val userDao : UserDao

    abstract  val productDao : ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}