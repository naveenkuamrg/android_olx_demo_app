package com.application.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.callbacks.ProductSummaryAdapterCallBack
import com.application.helper.Utility
import com.application.model.ProductSummary

class ProductSummaryAdapter(val callback: ProductSummaryAdapterCallBack) :
    RecyclerView.Adapter<ProductSummaryAdapter.ProductSummaryViewHolder>() {
    class ProductSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productId: Long = -1
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)

    }

    var data: List<ProductSummary> = listOf()

    private val diffUtil = object :
        DiffUtil.ItemCallback<ProductSummary>() {
        override fun areItemsTheSame(oldItem: ProductSummary, newItem: ProductSummary): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductSummary, newItem: ProductSummary): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)


    fun saveData(dataResponse: List<ProductSummary>) {
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSummaryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_summary_view, parent, false)



        return ProductSummaryViewHolder(itemView).apply {
            itemView.setOnClickListener {
                callback.callbackOnClick(productId)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ProductSummaryViewHolder, position: Int) {
        asyncListDiffer.currentList[position]
        holder.productId = asyncListDiffer.currentList[position].productId
        holder.imageView.setImageBitmap(asyncListDiffer.currentList[position].image)
        holder.title.text = asyncListDiffer.currentList[position].title
        holder.price.text =
            Utility.convertToINR(asyncListDiffer.currentList[position].price.toDouble())

    }
}