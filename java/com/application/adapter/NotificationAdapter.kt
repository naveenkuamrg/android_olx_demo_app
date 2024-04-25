package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.adapter.diffUtil.NotificationDiffUtil
import com.application.helper.Utility
import com.application.model.Notification
import com.application.model.NotificationType
import com.application.model.ProductListItem

class NotificationAdapter(val onItemClickListener: (Long) -> Unit) :
    PagingDataAdapter<Notification, RecyclerView.ViewHolder>(NotificationDiffUtil()) {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView = itemView.findViewById<TextView>(R.id.content_text_view)
        val timeStamp = itemView.findViewById<TextView>(R.id.time_stamp_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.notification_item, parent,
                false
            )
        return NotificationViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val value = getItem(position)
        if (holder is NotificationViewHolder) {
            holder.contentTextView.text = value!!.content
            holder.timeStamp.text =
                Utility.setCreatedTime(value.timestamp)
            if (value.isRead) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.md_theme_onSecondaryContainer_highContrast
                    )
                )
            } else {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.md_theme_primaryContainer
                    )
                )
            }
            holder.itemView.setOnClickListener {
                onItemClickListener(
                    value.id
                )
            }
        }
    }


}