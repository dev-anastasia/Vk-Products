package com.example.vk_products_app.network

import com.example.vk_products_app.entities.ProductsList
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

class ProductsRepository {

    fun getProductsList(skip: Int, limit: Int): Single<ProductsList> {
        return RetrofitObject.productsSearchService.getSearchResults(
            skip, limit
        )
    }
}