package com.example.vk_products_app.ui

sealed class SearchUIState<out Int> {
    object Loading : SearchUIState<Nothing>()
    object Success : SearchUIState<Nothing>()
    object Error : SearchUIState<Nothing>()
}
