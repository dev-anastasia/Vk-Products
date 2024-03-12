package com.example.vk_products_app

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

class SearchViewModel(private val repository: ProductsRemoteDataSource) : ViewModel() {

    val pagingData = MutableLiveData<PagingData<Product>>()

    val searchUiState: LiveData<SearchUIState<Int>>
        get() = _searchUiState
    private val _searchUiState = MutableLiveData<SearchUIState<Int>>(null)

    fun getProductsList(): Flow<PagingData<Product>> {
        changeUiState(LOADING)
        return repository.getProducts()
    }

    fun changeUiState(newState: String) {
        when (newState) {

            ERROR -> {
                _searchUiState.postValue(SearchUIState.Error)
            }

            NO_RESULTS -> {
                _searchUiState.postValue(SearchUIState.NoResults)
            }

            SUCCESS -> {
                _searchUiState.postValue(SearchUIState.Success)
            }

            LOADING -> {
                _searchUiState.postValue(SearchUIState.Loading)
            }
        }
    }

    companion object {

        const val LIMIT = 20
        const val ERROR = "ERROR"
        const val LOADING = "LOADING"
        const val SUCCESS = "SUCCESS"
        const val NO_RESULTS = "NO RESULTS"
        var SKIP = 0

        private val repo = ProductsRemoteDataSourceImpl()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(repo)
            }
        }
    }
}
