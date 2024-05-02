package com.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.application.R
import com.application.model.ProductType
import com.application.model.SearchProductResultItem

class SearchAdapter(private val onItemClickListener: (Long)->Unit) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    private val diffUtil = object :
        DiffUtil.ItemCallback<SearchProductResultItem>() {

        override fun areItemsTheSame(
            oldItem: SearchProductResultItem,
            newItem: SearchProductResultItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchProductResultItem,
            newItem: SearchProductResultItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun submitData(dataResponse: List<SearchProductResultItem>) {
        asyncListDiffer.submitList(dataResponse)
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productId: Long = -1L
        val productNameTextView = itemView.findViewById<TextView>(
            R.id.product_title_textview
        )
        val productCategoryTextView =
            itemView.findViewById<TextView>(R.id.product_category_textview)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.search_result_item,
            parent,
            false
        )
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.productId = data.id
        holder.productNameTextView.text =
            data.name
        holder.productCategoryTextView.text = ProductType.productTypeToString(data.type)
        holder.itemView.setOnClickListener {
            onItemClickListener(data.id)
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }


}