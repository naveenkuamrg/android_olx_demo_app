package com.application

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.application.fragments.LoginFragment
import com.application.fragments.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("mySharePref", MODE_PRIVATE)

        if(!sharedPreferences.getBoolean("dataInjected",false)){
            CoroutineScope(Dispatchers.Default).launch {
                InjectData(this@MainActivity).addSampleData()
            }
        }

        if (savedInstanceState == null) {
            if (sharedPreferences.getString("userId", "") == "") {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.main_view_container, LoginFragment())
                fragmentTransaction.commit()
            } else {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.main_view_container, MainFragment())
                fragmentTransaction.commit()
            }
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CALL_PHONE),
                    0
                )
            }
        }
    }
}