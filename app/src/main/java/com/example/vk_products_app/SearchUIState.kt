package com.example.vk_products_app

sealed class SearchUIState<out Int> {

    object Success : SearchUIState<Nothing>()

    object NoResults : SearchUIState<Nothing>()

    object Error : SearchUIState<Nothing>()
}
