package com.example.vk_products_app.ui.productsAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.R

class ProductRxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val thumbnail: ImageView = itemView.findViewById(R.id.iv_product_list_card_thumbnail)
    val description: TextView = itemView.findViewById(R.id.tv_product_list_card_description)
    val title: TextView = itemView.findViewById(R.id.tv_product_list_card_title)
}