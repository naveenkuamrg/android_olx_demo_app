package com.application.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.RemoveDataFromAdapterCallBack

class ImageViewAdapter(private val data: MutableList<Bitmap>) :
    RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {

    var callBack: RemoveDataFromAdapterCallBack? = null

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.product_image)
        val deleteImageView: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.product_image_view_item1,
            parent, false
        )
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.image.setImageBitmap(data[position])
        if(callBack != null){
            holder.deleteImageView.setOnClickListener {
                callBack?.removeDataFromList(position)
            }
        }else{
            holder.deleteImageView.visibility = View.GONE
        }
    }
}