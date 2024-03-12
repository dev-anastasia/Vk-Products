package com.example.vk_products_app.ui

import android.util.Log
import androidx.lifecycle.LiveData
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

class ProductsViewModel(private val repository: ProductsRemoteDataSource) : ViewModel() {

    val pagingData = MutableLiveData<PagingData<Product>>()
    val currentItem = MutableLiveData<Product>()
    val searchUiState: LiveData<SearchUIState<Int>>
        get() = _searchUiState
    private val _searchUiState = MutableLiveData<SearchUIState<Int>>(null)
    private val limit = 20

    fun getProductsList(queryText: String): Flow<PagingData<Product>> {
        changeUiState(LOADING_KEYWORD)
        return repository.getProducts(SKIP, queryText, limit)
    }

    fun changeUiState(newState: String) {
        when (newState) {

            ERROR_KEYWORD -> {
                SKIP = 0
                _searchUiState.postValue(SearchUIState.Error)
            }

            SUCCESS_KEYWORD -> {
                _searchUiState.postValue(SearchUIState.Success)
            }

            LOADING_KEYWORD -> {
                _searchUiState.postValue(SearchUIState.Loading)
            }

            else -> {
                Log.d("TAG", "Unknown SearchUiState")
            }
        }
    }

    companion object {
        var SKIP = 0
        const val ERROR_KEYWORD = "ERROR"
        const val LOADING_KEYWORD = "LOADING"
        const val SUCCESS_KEYWORD = "SUCCESS"

        private val repo = ProductsRemoteDataSourceImpl()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ProductsViewModel(repo)
            }
        }
    }
}
