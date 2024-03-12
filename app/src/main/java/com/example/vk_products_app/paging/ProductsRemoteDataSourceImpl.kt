package com.example.vk_products_app.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.vk_products_app.entities.Product
import kotlinx.coroutines.flow.Flow

class ProductsRemoteDataSourceImpl : ProductsRemoteDataSource {

    override fun getProducts(skip: Int, queryText: String, limit: Int): Flow<PagingData<Product>> {
        val source = ProductsPagingSource()
        source.addParams(queryText)
        return Pager(config = PagingConfig(pageSize = limit),
            pagingSourceFactory = { source }).flow
    }
}