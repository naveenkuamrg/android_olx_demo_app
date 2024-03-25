package com.application.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.ProductSummaryAdapterCallBack
import com.application.model.ProductSummary

class ProductSummaryAdapter(val callback : ProductSummaryAdapterCallBack) :
    RecyclerView.Adapter<ProductSummaryAdapter.ProductSummaryViewHolder>() {
    class ProductSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productId: Long = -1
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)

    }

    var  data: List<ProductSummary> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSummaryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_summary_view, parent, false)

        return ProductSummaryViewHolder(itemView).apply {
            itemView.setOnClickListener {
                Log.i("adapter",this.productId.toString())
                callback.callbackOnClick(productId)
            }
        }
    }

    override fun getItemCount(): Int {
        Log.i("TAGSize",data.size.toString())
       return data.size
    }

    override fun onBindViewHolder(holder: ProductSummaryViewHolder, position: Int) {
        holder.productId = data[position].productId
        holder.imageView.setImageBitmap(data[position].image)
        holder.title.text = data[position].title
        holder.price.text = "Rs: ${data[position].price}"
    }
}