package com.application.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.application.model.NotificationType
import java.sql.Date

@Entity(tableName = "notification", foreignKeys = [
    ForeignKey(User :: class, parentColumns = ["user_id"], childColumns = ["recipientId"], onDelete = ForeignKey.CASCADE)
])
data class Notification(
    val timestamp : String,
    val isRead : Boolean,
    val recipientId : Long,
    val notificationType : NotificationType ,
    val senderId : Long?,
    val productId: Long?
){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

}

