package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    }


    private val diffUtil = object : DiffUtil.ItemCallback<Notification>() {
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
        holder.contentTextView.text = asyncListDiffer.currentList[position].content
        holder.timeStamp.text =
            Utility.millisecondsToString(asyncListDiffer.currentList[position].timestamp)
        holder.itemView.setOnClickListener {
            onItemClickListener(
                asyncListDiffer.currentList[position].id,
                asyncListDiffer.currentList[position].type
            )
        }
    }
}