package com.example.vk_products_app.ui.imagesAdapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.R

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val productImageView: ImageView = itemView.findViewById(R.id.iv_product_image_info_page)
}