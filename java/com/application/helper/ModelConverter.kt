package com.application.helper

import android.graphics.Bitmap
import android.net.Uri
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.model.Product
import com.application.model.Profile

object ModelConverter {
    fun userEntityFromUserDetails(
        name: String,
        phoneNumber: String,
        email: String,
        password: String
    ): User {
        return User(
            name,
            phoneNumber,
            email,
            password
        )
    }

    fun profileFromUserAndUri(user: User): Profile {
        return Profile(user.id, user.name, null, user.phoneNumber, user.email)
    }

    fun productModelToProductDetails(product: Product): ProductDetails {
        return ProductDetails(
            product.title,
            product.price,
            product.postedDate.toString(),
            product.description,
            product.availabilityStatus,
            product.location,
            product.productType,
            product.sellerId
        )
    }
}


