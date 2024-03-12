package com.example.vk_products_app.ui

import com.example.vk_products_app.entities.Product

interface ItemClickListener {

    fun onClick(item: Product)
}