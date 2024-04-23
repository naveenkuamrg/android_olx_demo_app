package com.application.repositories.impl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.application.AppDatabase
import com.application.helper.ModelConverter
import com.application.helper.Utility
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.application.model.Notification
import com.application.model.NotificationType
import com.application.repositories.NotificationRepository

class NotificationRepositoryImpl(val context: Context) : NotificationRepository {

    private val notificationDao = AppDatabase.getInstance(context).notificationDao
    private val productImageRepository = ProductImageRepositoryImpl(context)
    private val profileImageRepository = ProfileImageRepositoryImpl(context)

    override fun getNotification(): LiveData<PagingData<Notification>> {


        return Pager(
            PagingConfig(
                20,
                20,
                enablePlaceholders = false
            )
        ) {
            notificationDao.getNotification(Utility.getLoginUserId(context))
        }.liveData.map { pagingData ->
            pagingData.map { notificationEntity ->
                ModelConverter.notificationEntityToNotificationModel(
                    notificationEntity
                ).apply {
                    val tempImage = if (type == NotificationType.PRODUCT) {
                        productImageRepository.getMainImage(
                            notificationEntity.productId.toString()
                        )
                    } else {
                        profileImageRepository.getProfileImage(
                            notificationEntity.senderId.toString()
                        )
                    }
                    if (tempImage != null) {
                        image = tempImage
                    }
                }
            }

        }

    }

    override suspend fun updateAllNotificationIsReadStatus() {
        notificationDao.updateAllNotificationIsRead(Utility.getLoginUserId(context))
    }

    override suspend fun isUnreadNotification(): Boolean {

        return notificationDao.getIsUnreadNotification(Utility.getLoginUserId(context))
    }

    override suspend fun updateNotificationIsReadStatus(notificationId: Long) {
        notificationDao.updateNotificationIsRead(notificationId)
    }


}