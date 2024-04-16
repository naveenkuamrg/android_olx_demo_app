package com.application.helper

import com.application.entity.Notification
import com.application.entity.ProductDetails
import com.application.entity.User
import com.application.entity.WishList
import com.application.entity.relations.ProductsWithInterestedProfile
import com.application.model.NotificationType
import com.application.model.Product
import com.application.model.Profile
import com.application.model.ProfileSummary
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
        content: String,
        senderId: Long
    ): Notification {
        return Notification(
            Date().time,
            false,
            recipientId,
            senderId,
            productId,
            content,
            type,
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

    fun wishListEntityBuilder(productId: Long, userId: Long): WishList {
        return WishList(userId, productId)
    }

    fun productsWithInterestedProfileSummary(model: ProductsWithInterestedProfile):
            List<ProfileSummary> {
        val listProfileSummary: MutableList<ProfileSummary> = mutableListOf()
        model.profileList.forEach { user ->
            var isContented: Boolean = false
            model.isContented.forEach {
                if(it.userId == user.id){
                    isContented = it.isContented
                    return@forEach
                }
            }
            listProfileSummary.add(
                ProfileSummary(
                    user.id,
                    user.name,
                    user.phoneNumber,
                    isContented
                )
            )
        }

        return listProfileSummary
    }
}


