package com.example.vk_products_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.paging.ProductsRemoteDataSource
import com.example.vk_products_app.paging.ProductsRemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow

class SearchViewModel(private val repository: ProductsRemoteDataSource) : ViewModel() {

    val pagingData = MutableLiveData<PagingData<Product>>()

    // val searchUiState = MutableLiveData<SearchUIState<Int>>(null)

    fun getProductsList(): Flow<PagingData<Product>> {
        return repository.getProducts()
    }

    companion object {

        const val LIMIT = 20
        var SKIP = 0

        private val repo = ProductsRemoteDataSourceImpl()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(repo)
            }
        }
    }
}
