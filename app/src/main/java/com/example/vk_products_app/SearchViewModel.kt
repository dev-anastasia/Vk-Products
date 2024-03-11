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

    val productsList = mutableListOf<Product>()

    val pagingData = MutableLiveData<List<Product>>()

    val searchUiState: LiveData<SearchUIState<Int>>
        get() {
            return _searchUiState
        }
    private val _searchUiState = MutableLiveData<SearchUIState<Int>>()

    fun getProductsList() : Flow<PagingData<Product>> {
        return repository.getProducts()
    }


//    fun getProductsList() {
//
//        ProductsRepository().getProductsList(SKIP, LIMIT)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ productsList ->
//                Log.d("LOG", "Reply")
//                if (productsList == null) {
//                    _searchUiState.postValue(SearchUIState.Error)
//                    return@subscribe
//                }
//                if (productsList.total == 0) {
//                    _searchUiState.postValue(SearchUIState.NoResults)
//                } else {
//                    this.productsList.addAll(productsList.products)
//                    _searchUiState.postValue(SearchUIState.Success)
//                    SKIP += 20
//                }
//            }, { error ->
//                _searchUiState.postValue(SearchUIState.Error)
//                Log.e("RxJava", "getProductsList() fun problem: $error")
//            })
//    }


//        val result = object: GetProductsRxRepository {
//            override fun getProducts(): Flowable<PagingData<Product>> {
//                TODO("Not yet implemented")
//            }
//
//        }

    companion object {
        //const val LIMIT = 20
        var SKIP = 0

        private val repo = ProductsRemoteDataSourceImpl()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(repo)
            }
        }
//        class SearchViewModelFactory (
//            //private val productsRepository: ProductsRepository
//            private val repository: GetProductsRxRepository
//        ) : ViewModelProvider.Factory {
//
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return SearchViewModel(repository) as T
//            }
//        }
    }
}
