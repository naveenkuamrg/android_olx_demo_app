package com.application.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.application.entity.Notification
import com.application.entity.User

//data class NotificationWithUser(
//    @Embedded˳˳
//    val notification : Notification,
//    @Relation(
//        parentColumn = "senderId",
//        entityColumn = "user_id"
//    )
//    val user : User
//)