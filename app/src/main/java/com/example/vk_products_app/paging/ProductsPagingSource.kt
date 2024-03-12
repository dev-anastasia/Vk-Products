package com.example.vk_products_app.paging

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.vk_products_app.ui.ProductsViewModel.Companion.SKIP
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.entities.ProductsList
import com.example.vk_products_app.network.RetrofitObject.productsSearchService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductsPagingSource : RxPagingSource<Int, Product>() {

    private val limit = 20
    private var queryText: String = ""

    fun addParams(queryText: String) { // Сюда передаётся текст запроса от пользователя
        this.queryText = queryText
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Product>> {
        return if (queryText.isEmpty()) {
            productsSearchService.getSearchResults(SKIP)
                .subscribeOn(Schedulers.io())
                .map { toLoadResult(it, SKIP) }
                .doOnSuccess { SKIP += 20 }
                .onErrorReturn { LoadResult.Error(it) }
        } else {
            productsSearchService.query(queryText, SKIP)
                .subscribeOn(Schedulers.io())
                .map { toLoadResult(it, SKIP) }
                .doOnSuccess { SKIP += 20 }
                .onErrorReturn { LoadResult.Error(it) }
        }
    }

    private fun toLoadResult(
        data: ProductsList,
        skip: Int
    ): LoadResult<Int, Product> {
        return LoadResult.Page(
            data = data.products,
            prevKey = if (data.total == 0 || skip < limit) null else skip - limit,
            nextKey = if (data.total == 0 || skip >= data.total) null else skip + limit,
        )
    }
}