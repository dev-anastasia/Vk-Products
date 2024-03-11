package com.example.vk_products_app.network

import com.example.vk_products_app.entities.ProductsList
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsSearchService {

    @GET("products/")
    fun getSearchResults(@Query("skip") skip: Int, @Query("limit") limit: Int = 20): Single<ProductsList>
}