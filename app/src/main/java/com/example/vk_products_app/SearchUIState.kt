package com.example.vk_products_app

sealed class SearchUIState<out Int> {

    data object Success : SearchUIState<Nothing>()
}
