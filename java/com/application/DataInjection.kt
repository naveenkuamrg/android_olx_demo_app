package com.application

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.application.model.AvailabilityStatus
import com.application.model.Product
import com.application.model.ProductType
import com.application.repositories.impl.AuthenticationRepositoryImpl
import com.application.repositories.impl.ProductRepositoryImpl
import com.application.repositories.impl.ProfileImageRepositoryImpl
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


class DataInjection(val context: Context) {


    val authenticationRepository = AuthenticationRepositoryImpl(context)
    val productRepository = ProductRepositoryImpl(context)
    val profileRepository = ProfileImageRepositoryImpl(context)

    suspend fun addSampleData() {
        authenticationRepository.setUserProfile(
            "viswa",
            "viswa@gmail.com",
            "9498292807",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "diwan",
            "diwan@gmail.com",
            "9997292897",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "naresh",
            "naresh@gmail.com",
            "9597292997",
            "Test@123"
        )

        authenticationRepository.setUserProfile(
            "Amal",
            "Amal123@gmail.com",
            "9493290800",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "shiva",
            "shiva@gmail.com",
            "9498290840",
            "Test@123"
        )
        authenticationRepository.setUserProfile(
            "abijith",
            "abijith@gmail.com",
            "9498790800",
            "Test@123"
        )

        authenticationRepository.setUserProfile(
            "naveen",
            "naveen123@gmail.com",
            "9798292800",
            "Naveen@123"
        )

        profileRepository.saveProfileImage("2", convertPngResourceToBitmap(R.raw.profile))
        profileRepository.saveProfileImage("3", convertPngResourceToBitmap(R.raw.profile1))
        profileRepository.saveProfileImage("4", convertPngResourceToBitmap(R.raw.person2))

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

        val product13 = Product(
            null,
            "Women's Convertible Hiking pats-lightweight Outdoor Trousers with Zip-off Legs",
            799.0,
            System.currentTimeMillis() - 604800000, // 7 days ago,
            "Stay comfortable and versatile on the trail with these convertible hiking pants. Lightweight and quick-drying fabric with zip-off legs for easy conversion to shorts. Ideal for hiking, backpacking, and camping.",
            AvailabilityStatus.AVAILABLE,
            "Sydney",
            ProductType.FASHION,
            2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.pant),
                convertPngResourceToBitmap(R.raw.pant1),
                convertPngResourceToBitmap(R.raw.pant)
            )
        }

        val product14 = Product(
            null,
            "Electric Kettle - 1.7L Stainless Strrl Water Boilder with Auto Shut - Off",
            3999.00,
            System.currentTimeMillis() - 604800000, // 7 days ago,,
            "Boil water quickly and safely with this stainless steel electric kettle. Features a large capacity and automatic shut-off for peace of mind. Perfect for making tea, coffee, and instant soups",
            AvailabilityStatus.AVAILABLE,
            "chennai",
            ProductType.APPLIANCES,
            1
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.electric_kettle),
                convertPngResourceToBitmap(R.raw.electric_kettle1),
            )
        }

        val product15 = Product(
            null,
            "Digital Kitchen Scale - Stainless Steel Food Scale with LCD Display",
            2999.00,
            System.currentTimeMillis() - 704800000,
            "Accurately measure ingredients for your recipes with this digital kitchen scale. Made from durable stainless steel with a sleek LCD display. Perfect for precise cooking and baking.",
            AvailabilityStatus.AVAILABLE,
            "Kanchipuram",
            ProductType.APPLIANCES,
            2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.digital_kitchen_scale),
                convertPngResourceToBitmap(R.raw.digital_kitchen_scale1),
                convertPngResourceToBitmap(R.raw.digital_kitchen_scale2)
            )
        }

        val product16 = Product(
            null,
            "Unisex Outdoor Backpack - Durable Hiking Pack with Multiple Compartments",
            3999.00,
            System.currentTimeMillis() - 705800000,
            "Carry all your essentials comfortably on your outdoor adventures with this durable hiking backpack. Features multiple compartments and adjustable straps for a customized fit. Perfect for hiking, camping, and travel.",
            AvailabilityStatus.AVAILABLE,
            "Chennai",
            ProductType.APPLIANCES,
            3
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.bag_1),
                convertPngResourceToBitmap(R.raw.bag_2),
                convertPngResourceToBitmap(R.raw.bag_3)
            )
        }

        val product17 = Product(
            null,
            "Non-Stick Cookware Set - 12 Piece Ceramic Coating Posts and Pans with Glass Lids",
            59999.00,
            System.currentTimeMillis() - 709800000,
            "Upgrade your cooking experience with this versatile cookware set. Features a durable ceramic coating for non-stick cooking and easy cleaning. Includes various sizes of pots and pans with heat-resistant glass lids.",
            AvailabilityStatus.AVAILABLE,
            "New York City",
            ProductType.APPLIANCES,
            3
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.pan),
                convertPngResourceToBitmap(R.raw.pan1),
                convertPngResourceToBitmap(R.raw.pan2)
            )
        }

        val product18 = Product(
            null,
            "Electric Stand Mixer 6 Speed Handheld Kitchen Mixer with Attachments",
            1599.00,
            System.currentTimeMillis() - 909800000,
            "Make baking a breeze with this powerful electric stand mixer. Equipped with multiple speed settings and versatile attachments for mixing dough, batter, and more. A must-have for any aspiring baker.",
            AvailabilityStatus.AVAILABLE,
            "Houston",
            ProductType.APPLIANCES,
            2
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.electric_stand_mixer),
                convertPngResourceToBitmap(R.raw.electric_stand_mixer1),
                convertPngResourceToBitmap(R.raw.electric_stand_mixer2),
                convertPngResourceToBitmap(R.raw.electric_stand_mixer3)
            )
        }

        val product19 = Product(
            null,
            "Stainless Steel Espresso Machine - Automatic Coffee Maker with Milk Forth",
            2999.99,
            System.currentTimeMillis() - 907800000,
            "Enjoy barista-quality coffee at home with this stainless steel espresso machine. Features automatic brewing and a built-in milk frother for creamy lattes and cappuccinos. Elevate your morning routine.",
            AvailabilityStatus.AVAILABLE,
            "San Francisco",
            ProductType.APPLIANCES,
            4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.automatic_coffee_make),
                convertPngResourceToBitmap(R.raw.automatic_coffee_make1)
            )
        }

        val product20 = Product(
            null,
            title = "Stainless Steel Water Bottle - Vacuum Insulated, Leak Proof",
            price = 699.99,
            postedDate = System.currentTimeMillis() - 345600000, // 4 days ago
            description = "Keep your drinks hot or cold for hours with this stainless steel water bottle. Double-wall vacuum insulation ensures temperature retention. Leak-proof design makes it ideal for outdoor activities.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Los Angeles",
            productType = ProductType.APPLIANCES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.bottle),
                convertPngResourceToBitmap(R.raw.bottle1)
            )
        }

        val product21 = Product(
            null,
            title = "Vegetable Spiralizer - 5 Blade Spiral Slicer",
            price = 19.99,
            postedDate = System.currentTimeMillis() - 518400000, // 6 days ago
            description = "Create healthy, delicious meals with this vegetable spiralizer. Includes 5 interchangeable blades for different spiral shapes. Perfect for making zucchini noodles, carrot spirals, and more.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Seattle",
            productType = ProductType.APPLIANCES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.vegetable_spiralizer),
                convertPngResourceToBitmap(R.raw.vegetable_spiralizer1),
                convertPngResourceToBitmap(R.raw.vegetable_spiralizer2)
            )
        }

        val product22 = Product(
            null,
            title = "Electric Scooter - Foldable Commuter Scooter",
            price = 33999.99,
            postedDate = System.currentTimeMillis() - 864000000, // 10 days ago
            description = "Commute in style with this electric scooter. Foldable design for easy storage and transportation. Lightweight yet durable construction.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Los Angeles",
            productType = ProductType.VEHICLES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.scooter),
                convertPngResourceToBitmap(R.raw.scooter1),
            )
        }

        val product23 = Product(
            null,
            title = "Mountain Bike - Full Suspension, Shimano Gears",
            price = 899.99,
            postedDate = System.currentTimeMillis() - 950400000, // 11 days ago
            description = "Conquer any trail with this full suspension mountain bike. Equipped with Shimano gears for smooth shifting. Durable frame built to withstand rough terrain.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Denver",
            productType = ProductType.VEHICLES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.bike),
                convertPngResourceToBitmap(R.raw.bike1),
                convertPngResourceToBitmap(R.raw.bike3),
            )
        }

        val product24 = Product(
            null,
            title = "Car Roof Rack - Universal Roof Mount Cargo Carrier",
            price = 149.99,
            postedDate = System.currentTimeMillis() - 1036800000, // 12 days ago
            description = "Expand your car's cargo capacity with this roof rack. Universal design fits most vehicles. Easy to install and remove for added convenience.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Seattle",
            productType = ProductType.VEHICLES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.car_roof),
                convertPngResourceToBitmap(R.raw.car_roof1),
                convertPngResourceToBitmap(R.raw.car_roof3)
            )
        }

        val product25 = Product(
            null,
            title = "Motorcycle Helmet - Full Face DOT Certified Helmet",
            price = 129.99,
            postedDate = System.currentTimeMillis() - 1123200000, // 13 days ago
            description = "Stay safe on the road with this full face motorcycle helmet. DOT certified for reliable protection. Ventilation system keeps you cool during long rides.",
            availabilityStatus = AvailabilityStatus.AVAILABLE,
            location = "Miami",
            productType = ProductType.VEHICLES,
            sellerId = 4
        ).apply {
            images = listOf(
                convertPngResourceToBitmap(R.raw.helmet)
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
                product12,
                product13,
                product14,
                product15,
                product16,
                product17,
                product18,
                product19,
                product20,
                product21,
                product22,
                product23,
                product24,
                product25,

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


    suspend fun makeGetApiRequest(url: URL, type: ProductType) {
        var httpURLConnection: HttpURLConnection? = null

        httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.setRequestProperty(
            "X-Master-Key",
            "\$2b\$10\$tJwbePxDZPklOJ4xffLoxeWQ.JkRAYdaObwskSbyzKzRTYoSa8qHK"
        )
        httpURLConnection.setRequestProperty(
            "X-Access-Key",
            "\$2a\$10\$.0k0NxettHv6FGtvts/mAOY7QguwOU/9AleIlTM4KOMSA/Q1/7XqO"
        )

        Log.i("TAG", "${httpURLConnection.responseCode.toString()}  url ${url.toString()}")

        val bufferReader = BufferedReader(
            InputStreamReader(httpURLConnection.inputStream)
        )
        val jsonStringHolder = StringBuilder()
        while (true) {
            val readLine = bufferReader.readLine()
            if (readLine == null) {
                break
            } else {
                jsonStringHolder.append(readLine)
            }
        }

        Log.i("Networking", jsonStringHolder.toString())
        val json = JSONObject(jsonStringHolder.toString())
        val products = JSONArray(json.getString("products").toString())
        Log.i("JSON", products.length().toString())
        for (i in 0..<products.length()) {
            val productJson = (products[i] as JSONObject)
            val userId = Random.nextLong(1, 8)
            val product = Product(
                null,
                productJson.getString("title"),
                productJson.getDouble("price"),
                Random.nextLong(
                    (System.currentTimeMillis() - 1123200000),
                    System.currentTimeMillis()
                ),
                productJson.getString("description"),
                AvailabilityStatus.AVAILABLE,
                "chennai",
                type,
                userId
            )

            val imagesUrls = productJson.get("images") as JSONArray
            val images = mutableListOf<Bitmap>()
            for (index in 0..<imagesUrls.length()) {
                val imageUrl = URL(imagesUrls[index] as String)
                val inputStream = imageUrl.openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                images.add(
                    bitmap
                )
            }
            productRepository.insertProduct(product.apply {
                this.images = images
            }).apply {
                val _product = Product(
                    this,
                    product.title,
                    product.price,
                    product.postedDate,
                    product.description,
                    product.availabilityStatus,
                    product.location,
                    product.productType,
                    product.sellerId
                )
                for (j in 1..Random.nextLong(1, 8)) {
                    if (j != userId) {
                        productRepository.updateProductIsInterested(j, _product, true)
                    }
                }
                for (j in 1..Random.nextLong(1, 8)) {
                    if (j != userId) {
                        productRepository.updateIsFavorite(_product, true, j)
                    }
                }
            }
        }
    }

}