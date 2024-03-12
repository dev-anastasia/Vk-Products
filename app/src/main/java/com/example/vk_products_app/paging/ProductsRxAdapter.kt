package com.example.vk_products_app.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.vk_products_app.R
import com.example.vk_products_app.adapter.ProductViewHolder
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.ui.ItemClickListener
import com.squareup.picasso.Picasso

class ProductsRxAdapter(private val listener: ItemClickListener) : PagingDataAdapter<Product, ProductViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_card, parent, false
        )
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = getItem(position)?.title

        holder.description.text = getItem(position)?.description

        Picasso.get()
            .load(getItem(position)?.thumbnail)
            .placeholder(R.drawable.thumbnail_placeholder)
            .into(holder.thumbnail)

        // ClickListener: нажатие на карточку товара
        holder.itemView.setOnClickListener {
            listener.onClick(getItem(position)!!)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}