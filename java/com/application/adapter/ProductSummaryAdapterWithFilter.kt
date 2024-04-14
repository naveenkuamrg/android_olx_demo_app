package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.R
import com.application.callbacks.OnFilterItemClickListener
import com.application.model.ProductListItem
import com.application.model.ProductType

class ProductSummaryAdapterWithFilter(
    private val itemClickListener: OnFilterItemClickListener,
    itemClick: (ProductListItem.ProductItem) -> Unit
) : ProductSummaryAdapter(itemClick) {

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vehicles = itemView.findViewById<View>(R.id.vehicles)
        val mobile = itemView.findViewById<View>(R.id.mobiles)
        val electronic = itemView.findViewById<View>(R.id.electronics)
        val furniture = itemView.findViewById<View>(R.id.furniture)
        val fashion = itemView.findViewById<View>(R.id.fashion)
        val books = itemView.findViewById<View>(R.id.books)
        val sport = itemView.findViewById<View>(R.id.sport)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else {
            2
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item_header, parent, false)
            return FilterViewHolder(itemView)
        }
        return super.onCreateViewHolder(parent, viewType)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is FilterViewHolder) {
            holder.vehicles.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.VEHICLES)
            }
            holder.mobile.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.MOBILES)
            }
            holder.electronic.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.ELECTRONICS_AND_APPLIANCES)
            }
            holder.furniture.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.FURNITURE)
            }
            holder.fashion.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.FASHION)
            }
            holder.books.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.BOOKS_SPORTS_AND_HOBBIES)
            }
            holder.sport.setOnClickListener {
                itemClickListener.onFilterItemClick(ProductType.SPORTS)
            }
        } else {
            super.onBindViewHolder(holder, position )
        }
    }


}