package com.example.vk_products_app.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.vk_products_app.entities.Product

class ProductDiffUtil(
    private val oldList: List<Product>,
    private val newList: List<Product>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].title == newList[newItemPosition].title
                        &&
                        oldList[oldItemPosition].description == newList[newItemPosition].description
                        &&
                        oldList[oldItemPosition].thumbnail == newList[newItemPosition].thumbnail)
    }
}