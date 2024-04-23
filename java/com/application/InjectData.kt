package com.application

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.paging.LOGGER
import com.application.helper.ImageConverter
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductType
import com.application.repositories.impl.AuthenticationRepositoryImpl
import com.application.repositories.impl.ProductRepositoryImpl
import com.application.repositories.impl.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class InjectData(val context: Context) {


    val authenticationRepository = AuthenticationRepositoryImpl(context)
    val productRepository = ProductRepositoryImpl(context)

    suspend fun addSampleData() {

        authenticationRepository.setUserProfile(
            "Test",
            "test120@gmail.com",
            "9498292807",
            "Test@123"
        )
//        authenticationRepository.setUserProfile(
//            "Test1",
//            "test128@gmail.com",
//            "9997292897",
//            "Test@123"
//        )
//        authenticationRepository.setUserProfile(
//            "Test2",
//            "test129@gmail.com",
//            "9597292997",
//            "Test@123"
//        )


        val product1 = Product(
            null,
            title = "Spacious SUV with Great Mileage and Advanced Safety Features - Perfect for Family Trips and Off-Road Adventures",
            price = 250000.0,
            postedDate = System.currentTimeMillis(),
            description = "Spacious SUV with great mileage and advanced safety features. Ideal for family trips and off-road adventures. This SUV combines rugged capability with refined comfort, making it the perfect choice for any journey. With ample cargo space and seating for up to seven passengers, you'll have plenty of room for everyone and everything you need. Plus, with advanced safety features like lane departure warning and automatic emergency braking, you can drive with confidence knowing you and your loved ones are protected. Experience the freedom of the open road with this versatile SUV!",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "California",
            productType = ProductType.VEHICLES,
            sellerId = 1
        ).apply {
            images = listOf(convertPngResourceToBitmap(R.raw.toyota))
        }

        val product2 = Product(
            null,
            title = "Latest iPhone 13 with Stunning Display and Powerful Performance - Experience the Future of Mobile Technology",
            price = 39999.0,
            postedDate = System.currentTimeMillis(),
            description = "Latest iPhone model with a stunning display and powerful performance. Experience the future of mobile technology with the iPhone 13. Featuring an edge-to-edge Super Retina XDR display, this phone delivers an immersive viewing experience for all your favorite content. With the powerful A15 Bionic chip, you can tackle even the most demanding tasks with ease, from gaming to multitasking. Plus, with advanced camera systems, you can capture stunning photos and videos in any lighting condition. Elevate your mobile experience with the iPhone 13!",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "New York",
            productType = ProductType.MOBILES,
            sellerId = 1
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.iphone1),
                convertPngResourceToBitmap(R.raw.iphone)
            )
        }

        val product3 = Product(
            null,
            title = "Energy-Efficient Refrigerator with Ample Storage Space - Keep Your Food Fresh and Organized",
            price = 3799.0,
            postedDate = System.currentTimeMillis(),
            description = "Energy-efficient refrigerator with ample storage space. Keep your food fresh and organized with this high-quality refrigerator. With adjustable shelves and door bins, you can customize the layout to fit your needs. The energy-efficient design helps reduce utility bills while minimizing environmental impact. Plus, with a spacious freezer section, you'll have plenty of room for frozen foods and ice cream treats. Upgrade your kitchen with this stylish and functional refrigerator today!",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Texas",
            productType = ProductType.APPLIANCES,
            sellerId = 1
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.refrigerator),
                convertPngResourceToBitmap(R.raw.refrigerator1)
            )
        }

        val product4 = Product(
            null,
            title = "Harry Potter Complete Book Set",
            price = 11339.0,
            postedDate = System.currentTimeMillis(),
            description = "Complete set of Harry Potter books, paperback edition.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "London",
            productType = ProductType.BOOKS,
            sellerId = 1,
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.book),
                convertPngResourceToBitmap(R.raw.book1),
                convertPngResourceToBitmap(R.raw.book3)
            )
        }

        val product5 = Product(
            null,
            title = "Adidas Running Shoes",
            price = 1799.99,
            postedDate = System.currentTimeMillis(),
            description = "Adidas running shoes for men, size 10, black.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Berlin",
            productType = ProductType.SPORTS,
            sellerId = 1,
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.shoe),
                convertPngResourceToBitmap(R.raw.shoe1),
                convertPngResourceToBitmap(R.raw.shoe3)
            )
        }

        val product6 = Product(
            null,
            title = "Leather Sofa Set",
            price = 15000.0,
            postedDate = System.currentTimeMillis(),
            description = "Premium quality leather sofa set, 3-seater, 2-seater, and single seater included.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Chicago",
            productType = ProductType.FURNITURE,
            sellerId = 1,
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.shopping),
                convertPngResourceToBitmap(R.raw.shopping2),
                convertPngResourceToBitmap(R.raw.shopping3)
            )
        }


        val productList =
            listOf(product1, product2, product3, product4, product5, product6)

        productList.forEach {
            productRepository.insertProduct(it)
        }
        context.getSharedPreferences("mySharePref", AppCompatActivity.MODE_PRIVATE).edit {
            putBoolean("dataInjected", true)
        }

    }

    private fun convertPngResourceToBitmap(resourceId: Int): Bitmap {
        return BitmapFactory.decodeResource(
                context.resources,
                resourceId)

    }

}