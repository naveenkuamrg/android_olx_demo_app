package com.application.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.R
import com.application.model.ProductListItem
import com.application.model.ProductListItem.ProductItem

 open class ProductSummaryAdapter(val itemClick:(ProductItem)->Unit) :
    PagingDataAdapter<ProductListItem,
            ViewHolder>(object: DiffUtil.ItemCallback<ProductListItem>(){
        override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem == newItem
        }

    }) {
    class ProductSummaryViewHolder(itemView: View) : ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)
    }


     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item = getItem(position) as ProductItem
         if(holder is ProductSummaryViewHolder) {
             holder.title.text = item.title
             holder.price.text = item.price
             holder.imageView.setImageBitmap(item.image)
             holder.itemView.setOnClickListener {
                     itemClick(item)
             }
         }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_summary_view, parent, false)
        return ProductSummaryViewHolder(view)
    }

     override fun getItemCount(): Int {
         Log.i("naveen", "observe ${super.getItemCount()}")
         return super.getItemCount()
     }
}