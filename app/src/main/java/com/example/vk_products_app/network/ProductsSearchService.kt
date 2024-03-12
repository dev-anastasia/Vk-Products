package com.example.vk_products_app.network

import com.example.vk_products_app.entities.ProductsList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsSearchService {

    @GET("products/")
    fun getSearchResults(@Query("skip") skip: Int, @Query("limit") limit: Int = 20)
            : Single<ProductsList>

    @GET("products/search")
    fun query(
        @Query("q") query: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int = 20
    ) : Single<ProductsList>
}