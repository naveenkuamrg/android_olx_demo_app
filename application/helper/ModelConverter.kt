package com.application.helper

import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList
import com.application.model.NotificationType
import com.application.model.Product
import com.application.model.Profile
import java.util.Date

object ModelConverter {
    fun buildUserEntityFromUserDetails(
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
            product.postedDate,
            product.description,
            product.availabilityStatus,
            product.location,
            product.productType,
            product.sellerId
        ).apply {
            if (product.id != null) {
                id = product.id
            }
        }
    }

    fun notificationBuilder(
        recipientId: Long,
        productId: Long,
        type: NotificationType,
        content: String
    ): Notification {
        return Notification(
            Date().time,
            false,
            recipientId,
            productId,
            content,
            type
        )
    }

    fun userEntityToProfile(user: User): Profile {
        return Profile(user.id, user.name, null, user.phoneNumber, user.email)
    }

    fun notificationEntityToNotificationModel(notification: Notification):
            com.application.model.Notification {
        return com.application.model.Notification(
            notification.id,
            notification.timestamp,
            notification.isRead,
            notification.content,
            notification.type
        )
    }

    fun wishListEntityBuilder(productId: Long,userId: Long): WishList{
        return WishList(userId,productId)
    }

}


