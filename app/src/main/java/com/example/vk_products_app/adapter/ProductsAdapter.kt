package com.example.vk_products_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.R
import com.squareup.picasso.Picasso

class ProductsAdapter : RecyclerView.Adapter<ProductViewHolder>() {

    private val list: MutableList<Product> = mutableListOf()

//    private val dummyList = listOf(
//        Product(1, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(2, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(3, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(4, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(5, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(6, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(7,  "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(8, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
//        Product(9, "iPhone", "A very nice one", "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg")
//    )

    fun updateList(newList: List<Product>) {
        Log.d("TAG", "adapter updateList")
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_card_view, parent, false
        )
        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.title.text = list[holder.adapterPosition].title

        holder.description.text = list[holder.adapterPosition].description

        Picasso.get()
            .load(list[holder.adapterPosition].thumbnail)
            .into(holder.thumbnail)

        // Далее будет clickListener - открытие страницы товара
    }


}