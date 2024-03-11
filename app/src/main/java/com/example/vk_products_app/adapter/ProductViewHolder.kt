package com.example.vk_products_app.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.R
import com.squareup.picasso.Picasso

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val thumbnail: ImageView = itemView.findViewById(R.id.iv_product_list_card_thumbnail)
    val description: TextView = itemView.findViewById(R.id.tv_product_list_card_description)
    val title: TextView = itemView.findViewById(R.id.tv_product_list_card_title)

    fun updateTitle(newTitle: String) {
        title.text = newTitle
    }

    fun updateDescription(newDescr: String) {
        description.text = newDescr
    }

    fun updateThumbnail(newThumb: String) {
        Picasso.get()
            .load(newThumb)
            .placeholder(R.drawable.thumbnail_placeholder)
            .into(thumbnail)
    }
}