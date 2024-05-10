package com.application

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.application.fragments.LoginFragment
import com.application.fragments.MainFragment
import com.application.fragments.PrefetchingData
import com.application.model.ProductType
import com.application.viewmodels.PrefetchingDataViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val prefetchingDataViewModel: PrefetchingDataViewModel by viewModels {
        PrefetchingDataViewModel.FACTORY
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("mySharePref", MODE_PRIVATE)

        if (!sharedPreferences.getBoolean("dataInjected", false)) {
            prefetchingDataViewModel.loading.postValue(true)
            lifecycleScope.launch(Dispatchers.IO) {
                DataInjection(this@MainActivity).addSampleData()
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://dummyjson.com/products/category/smartphones"),
                    ProductType.MOBILES
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://dummyjson.com/products/category/home-decoration"),
                    ProductType.FURNITURE
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://dummyjson.com/products/category/fragrances"),
                    ProductType.OTHERS
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://dummyjson.com/products/category/laptops"),
                    ProductType.OTHERS
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/663471bfad19ca34f863c34e?meta=false"),
                    ProductType.FASHION
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/66347d0ce41b4d34e4ee132a?meta=false"),
                    ProductType.OTHERS
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/66348338e41b4d34e4ee1518?meta=false"),
                    ProductType.MOBILES
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/66348b40e41b4d34e4ee17c1?meta=false"),
                    ProductType.BOOKS
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/6634a9ace41b4d34e4ee2266?meta=false"),
                    ProductType.SPORTS
                )
                DataInjection(this@MainActivity).makeGetApiRequest(
                    URL("https://api.jsonbin.io/v3/b/6634b707acd3cb34a84252bf?meta=false"),
                    ProductType.FURNITURE
                )
                prefetchingDataViewModel.loading.postValue(false)
            }
        }else{
            prefetchingDataViewModel.loading.postValue(false)
        }
//        lifecycleScope.launch(Dispatchers.IO) {
//            DataInjection(this@MainActivity).makeGetApiRequest(
//                URL("https://api.jsonbin.io/v3/b/6634b707acd3cb34a84252bf?meta=false"),
//                ProductType.FURNITURE
//            )
//        }

        if (savedInstanceState == null) {
            prefetchingDataViewModel.loading.observe(this){
                if(it){
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.main_view_container,PrefetchingData())
                    fragmentTransaction.commit()
                }else{
                    if (sharedPreferences.getString("userId", "") == "") {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.main_view_container, LoginFragment())
                        fragmentTransaction.commit()
                    } else {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.main_view_container, MainFragment())
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
    }
}