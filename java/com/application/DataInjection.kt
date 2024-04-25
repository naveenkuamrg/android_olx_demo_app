package com.application

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductType
import com.application.repositories.impl.AuthenticationRepositoryImpl
import com.application.repositories.impl.ProductRepositoryImpl

class DataInjection(val context: Context) {


    val authenticationRepository = AuthenticationRepositoryImpl(context)
    val productRepository = ProductRepositoryImpl(context)

    suspend fun addSampleData() {
        authenticationRepository.setUserProfile(
            "Test",
            "test120@gmail.com",
            "9498292807",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "Test1",
            "test128@gmail.com",
            "9997292897",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "Test2",
            "test129@gmail.com",
            "9597292997",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "naveen",
            "naveen123@gmail.com",
            "9498292800",
            "Naveen@123"
        )

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

        val product7 = Product(
            null,
            title = "Men's Waterproof Hiking Boots - Breathable Outdoor Adventure Shoes with Ankle Support",
            price = 799.99,
            postedDate = System.currentTimeMillis() - 86400000, // 1 day ago
            description = "Sturdy and comfortable hiking boots designed for outdoor adventures. Waterproof construction keeps your feet dry in wet conditions. Features ankle support for stability and breathability for comfort on long hikes.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Denver",
            productType = ProductType.FASHION,
            sellerId = 2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.shoe4),
                convertPngResourceToBitmap(R.raw.shoe5),
                convertPngResourceToBitmap(R.raw.shoe6)
            )
        }

        val product8 = Product(
            null,
            title = "Wireless Noise-Canceling Headphones - Over-Ear Bluetooth Headset with Hi-Fi Sound",
            price = 1490.99,
            postedDate = System.currentTimeMillis() - 172800000, // 2 days ago
            description = "Immerse yourself in your favorite music with these wireless headphones. Enjoy high-fidelity sound quality and active noise cancellation technology for a truly immersive listening experience. Perfect for travel, work, or relaxation.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "San Francisco",
            productType = ProductType.OTHERS,
            sellerId = 2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.headset),
                convertPngResourceToBitmap(R.raw.headset2),
                convertPngResourceToBitmap(R.raw.headset3)
            )
        }

        val product9 = Product(
            null,
            title = "Professional Chef's Knife Set - Premium Stainless Steel Knives with Wooden Handles",
            price = 1290.99,
            postedDate = System.currentTimeMillis() - 259200000, // 3 days ago
            description = "Upgrade your kitchen with this high-quality knife set. Includes essential knives for slicing, dicing, and chopping. Made from premium stainless steel with comfortable wooden handles. Ideal for professional chefs and home cooks alike.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Chicago",
            productType = ProductType.APPLIANCES,
            sellerId = 2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.knife),
                convertPngResourceToBitmap(R.raw.knife1)
            )
        }

        val product10 = Product(
            null,
            title = "Vintage Leather Messenger Bag - Handcrafted Satchel for Laptops and Documents",
            price = 4500.0,
            postedDate = System.currentTimeMillis() - 432000000, // 5 days ago
            description = "Classic and stylish leather messenger bag perfect for carrying laptops, documents, and essentials. Handcrafted from genuine leather with a vintage finish. Features multiple compartments and adjustable shoulder straps.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "London",
            productType = ProductType.FASHION,
            sellerId = 3
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.bag),
                convertPngResourceToBitmap(R.raw.bag1),
                convertPngResourceToBitmap(R.raw.bag2)
            )
        }

        val product11 = Product(
            null,
            title = "Smart LED Light Bulb - WiFi Enabled Dimmable Bulb with Voice Control",
            price = 600.99,
            postedDate = System.currentTimeMillis() - 518400000, // 6 days ago
            description = "Transform your home lighting with these smart LED bulbs. Connect to your WiFi network for easy control via smartphone app or voice commands with compatible assistants. Customize brightness, color, and schedules for ultimate convenience.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Tokyo",
            productType = ProductType.APPLIANCES,
            sellerId = 3
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.light),
                convertPngResourceToBitmap(R.raw.light1),
                convertPngResourceToBitmap(R.raw.light2)
            )
        }

        val product12 = Product(
            null,
            title = "Yoga Mat with Alignment Lines - Non-Slip Exercise Mat for Home or Studio Use",
            price = 700.99,
            postedDate = System.currentTimeMillis() - 604800000, // 7 days ago
            description = "Enhance your yoga practice with this premium yoga mat featuring alignment lines for proper positioning. Made from eco-friendly materials with a non-slip surface for stability during poses. Suitable for all levels and styles of yoga.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Sydney",
            productType = ProductType.SPORTS,
            sellerId = 3
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.mate),
                convertPngResourceToBitmap(R.raw.mate1),
                convertPngResourceToBitmap(R.raw.mate2),
                convertPngResourceToBitmap(R.raw.mate3)
            )
        }

        val productList =
            listOf(
                product1,
                product2,
                product3,
                product4,
                product6,
                product7,
                product8,
                product9,
                product10,
                product11,
                product12
            )

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
            resourceId
        )

    }

}