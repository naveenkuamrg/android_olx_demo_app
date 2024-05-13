package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.adapter.diffUtil.NotificationDiffUtil
import com.application.compose.NotificationItemView
import com.application.helper.Utility
import com.application.model.Notification

class NotificationAdapter(val onItemClickListener: (Long) -> Unit) :
    PagingDataAdapter<Notification, RecyclerView.ViewHolder>(NotificationDiffUtil()) {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView = itemView.findViewById<TextView>(R.id.content_text_view)
        val timeStamp = itemView.findViewById<TextView>(R.id.time_stamp_text_view)
//        val image = itemView.findViewById<ShapeableImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.notification_item, parent,
                false
            )
        return NotificationViewHolder(itemView)
    }

    //    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int,
//    ): MyComposeViewHolder {
//        return MyComposeViewHolder(ComposeView(parent.context))
//    }
    class MyComposeViewHolder(
        private val composeView: ComposeView
    ) : RecyclerView.ViewHolder(composeView) {


        fun bind(notification: Notification, onItemClickListener: (Long) -> Unit) {
            composeView.setContent {
                NotificationItemView(notification, onItemClickListener)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val value = getItem(position)


//        if(holder is MyComposeViewHolder){
//            holder.bind(value!!){
//                onItemClickListener(
//                    value.id
//                )
//            }
//        }

        if (holder is NotificationViewHolder) {
//            val _image = value!!.image
//            if (_image != null) {
//                holder.image.setImageBitmap(_image)
//            } else {
//                if (value.type == NotificationType.PRODUCT) {
//                    holder.image.setImageResource(R.drawable.ic_delete_forevere)
//                } else {
//                    holder.image.setImageResource(R.drawable.ic_person)
//                }
//            }
//            if (value.type == NotificationType.PRODUCT) {
//                holder.image.shapeAppearanceModel = ShapeAppearanceModel.builder()
//                    .setAllCorners(CornerFamily.ROUNDED,50F )
//                    .build()
//            } else {
//                holder.image.setImageResource(R.drawable.ic_person)
//            }
            holder.contentTextView.text = value!!.content
            holder.timeStamp.text =
                Utility.setCreatedTime(value.timestamp)
            if (value.isRead) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.md_theme_background
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