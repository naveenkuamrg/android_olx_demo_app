package com.application.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.application.model.Notification

class NotificationDiffUtil : DiffUtil.ItemCallback<Notification>(){
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

}