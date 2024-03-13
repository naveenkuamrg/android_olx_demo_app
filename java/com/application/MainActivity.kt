package com.application

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import com.application.entity.InterestedList
import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList
import com.application.fragments.HomeFragment
import com.application.fragments.LoginFragment
import com.application.fragments.MainFragment
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
        val sharedPreferences = getSharedPreferences("mySharePref", MODE_PRIVATE)

        if(savedInstanceState == null){

            if(sharedPreferences.getString("userId","") == "") {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.main_view_container, LoginFragment())
                fragmentTransaction.commit()
            }else{
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.main_view_container,MainFragment())
                fragmentTransaction.commit()
            }
        }

    }
}