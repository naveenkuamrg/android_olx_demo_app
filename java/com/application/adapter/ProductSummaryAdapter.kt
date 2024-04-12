package com.application.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.R
import com.application.callbacks.OnItemClickListener
import com.application.helper.Utility
import com.application.model.ProductListItem
import com.application.model.ProductListItem.ProductItem

open class ProductSummaryAdapter(protected val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ViewHolder>() {


    class ProductSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)

    }


    private val diffUtil = object :
        DiffUtil.ItemCallback<ProductListItem>() {

        override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductListItem,
            newItem: ProductListItem
        ): Boolean {
            return oldItem == newItem
        }

    }


    private val asyncListDiffer = AsyncListDiffer(
        this,
        diffUtil
    )


    open fun submitData(dataResponse: List<ProductListItem>) {
        asyncListDiffer.submitList(dataResponse)
//        data = dataResponse
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product_summary_view, parent, false)


        return ProductSummaryViewHolder(itemView).apply {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        if (holder is ProductSummaryViewHolder && data is ProductItem) {
            holder.imageView.setImageBitmap(data.image)
            holder.title.text = data.title
            holder.price.text =
                Utility.convertToINR(data.price.toDouble())
        }

    }
}

 class ProductSummaryAdapter1(
     protected val itemClickListener: OnItemClickListener,
     diffCallback: DiffUtil.ItemCallback<ProductItem>
) :
    PagingDataAdapter<ProductItem,
            ProductSummaryAdapter1.ProductSummaryViewHolder>(diffCallback) {
    class ProductSummaryViewHolder(itemView: View) : ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)
    }
    override fun onBindViewHolder(holder: ProductSummaryViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item?.title
        holder.price.text = item?.price
        holder.imageView.setImageBitmap(item?.image)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_summary_view, parent, false)
        return ProductSummaryViewHolder(view).apply {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}