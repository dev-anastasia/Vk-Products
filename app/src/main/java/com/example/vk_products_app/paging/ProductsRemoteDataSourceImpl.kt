package com.example.vk_products_app.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.example.vk_products_app.entities.Product
import kotlinx.coroutines.flow.Flow

class ProductsRemoteDataSourceImpl : ProductsRemoteDataSource {

    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ProductsPagingSource() }).flow
    }
}