package com.example.vk_products_app.entities

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val brand: String,
    val thumbnail: String,
    val images: List<String>
)