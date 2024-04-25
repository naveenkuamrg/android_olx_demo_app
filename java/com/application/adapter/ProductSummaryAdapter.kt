package com.application.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.R
import com.application.adapter.diffUtil.ProductSummaryDiffUtil
import com.application.helper.Utility
import com.application.model.ProductListItem
import com.application.model.ProductListItem.ProductItem
import com.application.model.ProductType

class ProductSummaryAdapter(private val onItemClickListener: (ProductItem) -> Unit) :
    PagingDataAdapter<ProductListItem,
            ViewHolder>(ProductSummaryDiffUtil()) {
    class ProductSummaryViewHolder(itemView: View) : ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.product_main_image_view)
        val title = itemView.findViewById<TextView>(R.id.product_title_textview)
        val price = itemView.findViewById<TextView>(R.id.product_price_textview)
    }

    class DividerViewHolder(itemView: View) : ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.titleDivider)
    }

    class FilterViewHolder(itemView: View) : ViewHolder(itemView) {
        val vehicles = itemView.findViewById<View>(R.id.vehicles)
        val mobile = itemView.findViewById<View>(R.id.mobiles)
        val electronic = itemView.findViewById<View>(R.id.electronics)
        val furniture = itemView.findViewById<View>(R.id.furniture)
        val fashion = itemView.findViewById<View>(R.id.fashion)
        val books = itemView.findViewById<View>(R.id.books)
        val sport = itemView.findViewById<View>(R.id.sport)
    }

    lateinit var onFilterClickListener: (ProductType) -> Unit
    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is ProductListItem.Header -> {
                HEADER
            }

            is ProductListItem.Divider -> {
                DIVIDER
            }

            is ProductItem -> {
                PRODUCT_ITEM
            }

            null -> {
                Int.MIN_VALUE
            }
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (holder) {
            is FilterViewHolder -> {
                holder.vehicles.setOnClickListener {
                    onFilterClickListener(ProductType.VEHICLES)
                }
                holder.mobile.setOnClickListener {
                    onFilterClickListener(ProductType.MOBILES)
                }
                holder.electronic.setOnClickListener {
                    onFilterClickListener(ProductType.APPLIANCES)
                }
                holder.furniture.setOnClickListener {
                    onFilterClickListener(ProductType.FURNITURE)
                }
                holder.fashion.setOnClickListener {
                    onFilterClickListener(ProductType.FASHION)
                }
                holder.books.setOnClickListener {
                    onFilterClickListener(ProductType.BOOKS)
                }
                holder.sport.setOnClickListener {
                    onFilterClickListener(ProductType.SPORTS)
                }
            }

            is ProductSummaryViewHolder -> {
                val item = getItem(position) as ProductItem
                holder.title.text = item.title
                holder.price.text = Utility.convertToINR(item.price.toDouble())
                holder.imageView.setImageBitmap(item.image)
                holder.itemView.setOnClickListener {
                    onItemClickListener(item)
                }
            }

            is DividerViewHolder -> {
                val item = getItem(position) as ProductListItem.Divider
                holder.textView.text = item.title
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            HEADER -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_item_header, parent, false)
                FilterViewHolder(itemView)
            }
            PRODUCT_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_summary_view, parent, false)
                ProductSummaryViewHolder(view)
            }
            DIVIDER -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.divider, parent,
                        false
                    )
                DividerViewHolder(view)
            }

            else -> {
                throw Exception("View Type is not found")
            }
        }

    }

    companion object {
        const val PRODUCT_ITEM = 1
        const val HEADER = 2
        const val DIVIDER = 3
    }

}