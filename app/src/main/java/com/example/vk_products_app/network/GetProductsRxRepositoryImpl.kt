package com.example.vk_products_app.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.vk_products_app.entities.Product
import io.reactivex.Flowable

//class GetProductsRxRepositoryImpl (private val pagingSource: ProductsRxPagingSource) :
//    GetProductsRxRepository {
//    override fun getProducts(): Flowable<PagingData<Product>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 20
//            ), pagingSourceFactory = { pagingSource }
//        ).flowable
//    }
//}