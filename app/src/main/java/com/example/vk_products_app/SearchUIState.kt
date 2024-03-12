package com.example.vk_products_app

sealed class SearchUIState<out Int> {
    object Loading : SearchUIState<Nothing>()
    object Success : SearchUIState<Nothing>()
    object NoResults : SearchUIState<Nothing>()
    object Error : SearchUIState<Nothing>()
}
