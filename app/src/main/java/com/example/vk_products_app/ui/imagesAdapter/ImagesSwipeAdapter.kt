package com.example.vk_products_app.ui.imagesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.R
import com.squareup.picasso.Picasso

class ImagesSwipeAdapter(private val listOfImages: List<String>) :
    RecyclerView.Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_image_view, parent, false
        )
        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int = listOfImages.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Picasso.get()
            .load(listOfImages[position])
            .placeholder(R.drawable.thumbnail_placeholder)
            .into(holder.productImageView)
    }
}