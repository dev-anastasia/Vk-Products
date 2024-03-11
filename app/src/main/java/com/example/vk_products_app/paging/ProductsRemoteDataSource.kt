package com.example.vk_products_app.paging

import androidx.paging.PagingData
import com.example.vk_products_app.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {

    fun getProducts(): Flow<PagingData<Product>>
}