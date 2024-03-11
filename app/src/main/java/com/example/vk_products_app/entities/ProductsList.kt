package com.example.vk_products_app.entities

import com.example.vk_products_app.entities.Product

data class ProductsList(
    val products: List<Product>,
    val total: Int
)
