package com.application.helper

import com.application.model.NotificationType

object NotificationContentBuilder {
    fun build(type: NotificationType,
              senderName: String,
              productName: String,
              isInterested: Boolean? = null): String{

       return when(type){
            NotificationType.PROFILE->{
                if(isInterested == true) {
                    "$senderName is interested in your product $productName"
                }else{
                    "$senderName is removed from interested your product $productName"
                }
            }
            NotificationType.PRODUCT->{
                "$productName is mark as sold by $senderName"
            }
        }
    }

}