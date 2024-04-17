package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.helper.Utility
import com.application.model.Notification
import com.application.model.NotificationType

class NotificationAdapter(val onItemClickListener: (Long, NotificationType) -> Unit) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView = itemView.findViewById<TextView>(R.id.content_text_view)
        val timeStamp = itemView.findViewById<TextView>(R.id.time_stamp_text_view)
        val image: ImageView = itemView.findViewById(R.id.notification_ic)
    }


    private val diffUtil = object :
        DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun submitData(dataResponse: List<Notification>) {
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.notification_item, parent,
                false
            )
        return NotificationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val value = asyncListDiffer.currentList[position]
        holder.contentTextView.text = value.content
        holder.timeStamp.text =
            Utility.setCreatedTime(value.timestamp)
        if (value.image == null) {
            if (value.type == NotificationType.PROFILE) {
                holder.image.setImageResource(R.drawable.ic_profile_outline)
            } else {
                holder.image.setImageResource(R.drawable.ic_delete_forevere)
            }
        } else {
            holder.image.setImageBitmap(value.image)
        }
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
                value.id,
                value.type
            )
        }
    }
}