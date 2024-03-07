package com.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.application.entity.InterestedList
import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList
import com.application.model.AvailabilityStatus
import com.application.model.NotificationType
import com.application.model.ProductType

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = AppDatabase.getInstance(this)
        val dao = db.userDao
        CoroutineScope(Dispatchers.IO).launch {
//            db.userDao.insertUser(User("naveen", "9080534108", "milkynaveen@gamil.com", "Gsum@thi"))
//            dao.insertProduct(ProductDetails("Title","2000","date","description",
//                AvailabilityStatus.AVAILABLE,"location", ProductType.CAR,1))
//            dao.insertWishList(WishList(1,1))
//            dao.insertInterestedList(InterestedList(1,1))
//            dao.insertNotification(Notification("1000",true,1,NotificationType.PRODUCT,1,1))

            Log.i("Tag", db.userDao.getProfile().toString())
            Log.i("Tag",dao.getProduct().toString())
            Log.i("Tag",dao.getWishList(1).toString())
            Log.i("Tag",dao.getProductWithProfile(1).toString())
            Log.i("TAG",dao.getProfileWithProduct(1).toString())
            Log.i("TAG",dao.getNotificationWithProduct(1).toString())
        }
    }
}