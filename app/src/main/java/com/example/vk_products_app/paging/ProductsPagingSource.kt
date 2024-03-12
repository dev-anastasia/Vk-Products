package com.example.vk_products_app.paging

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.vk_products_app.ProductsListFragment.Companion.CURRENT_QUERY
import com.example.vk_products_app.SearchViewModel.Companion.LIMIT
import com.example.vk_products_app.SearchViewModel.Companion.SKIP
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.entities.ProductsList
import com.example.vk_products_app.network.RetrofitObject.productsSearchService
import io.reactivex.Single

class ProductsPagingSource : RxPagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Product>> {
        return if (CURRENT_QUERY.isEmpty()) {
            Log.d("TAG", "loadSingle isEmpty")
            productsSearchService.getSearchResults(SKIP)
                .map { toLoadResult(it, SKIP) }
                .doOnSuccess { SKIP += LIMIT }
                .onErrorReturn { LoadResult.Error(it) }
        } else {
            Log.d("TAG", "loadSingle is NOT empty")
            productsSearchService.query(CURRENT_QUERY, SKIP)
                .map { toLoadResult(it, SKIP) }
                .doOnSuccess { SKIP += LIMIT }
                .onErrorReturn { LoadResult.Error(it) }
        }
    }

    private fun toLoadResult(
        data: ProductsList,
        position: Int
    ): LoadResult<Int, Product> {
        return LoadResult.Page(
            data = data.products,
            prevKey = if (position == 0) null else position - LIMIT,
            nextKey = if (position >= data.total) null else position + LIMIT,
        )
    }
}