package com.example.vk_products_app.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.R
import com.example.vk_products_app.ui.SearchUIState
import com.example.vk_products_app.ui.ProductsViewModel
import com.example.vk_products_app.ui.ProductsViewModel.Companion.ERROR_KEYWORD
import com.example.vk_products_app.ui.ProductsViewModel.Companion.SKIP
import com.example.vk_products_app.ui.ProductsViewModel.Companion.SUCCESS_KEYWORD
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.ui.productsAdapter.ProductsRxAdapter
import com.example.vk_products_app.ui.productsAdapter.ItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsListFragment : Fragment(R.layout.fragment_products_list), ItemClickListener {

    private lateinit var vm: ProductsViewModel
    private var currentQueryText = ""
    private var queryRunnable: Runnable? = null
    private var queryCoroutine: Job? = null
    private var productsAdapter: ProductsRxAdapter? = null
    private val delayTimer = 2000L

    override fun onAttach(context: Context) {
        super.onAttach(context)

        vm = ViewModelProvider(
            requireActivity(), ProductsViewModel.Factory
        )[ProductsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingIcon = requireView().findViewById<ImageView>(R.id.iv_loading)

        // Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_products_list)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        productsAdapter = ProductsRxAdapter(this)
        recyclerView.adapter = productsAdapter

        // Observers
        vm.searchUiState.observe(viewLifecycleOwner) {
            when (it) {
                SearchUIState.Loading -> {
                    hideSoftKeyboard()
                    loadingIcon.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }

                SearchUIState.Error -> {

                    loadingIcon.visibility = View.GONE
                    Toast.makeText(
                        activity, "Непредвиденная ошибка", Toast.LENGTH_SHORT
                    ).show()
                    Log.e("TAG", "$it")
                }

                SearchUIState.Success -> {
                    loadingIcon.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                else -> {
                    Log.d("TAG", "Unknown SearchUiState")
                }
            }
        }

        // Запрос в сеть: получение списка товаров
        CoroutineScope(Dispatchers.IO).launch {
            if (vm.pagingData.value != null) {
                productsAdapter!!.submitData(vm.pagingData.value!!)
                vm.changeUiState(SUCCESS_KEYWORD)
            } else {
                val flow = vm.getProductsList(currentQueryText)
                getAndSubmitResults(flow)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        queryRunnable = Runnable {
            if (currentQueryText.isEmpty().not()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val flow = vm.getProductsList(currentQueryText)
                    getAndSubmitResults(flow)
                }
            }
        }

        requireView().findViewById<SearchView>(R.id.sv_search)
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    currentQueryText = query ?: ""
                    queryCoroutine?.cancel()
                    queryCoroutine = CoroutineScope(Dispatchers.IO).launch {
                        SKIP = 0
                        queryRunnable?.run()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    currentQueryText = newText ?: ""
                    queryCoroutine?.cancel()
                    queryCoroutine = CoroutineScope(Dispatchers.IO).launch {
                        delay(delayTimer)
                        SKIP = 0
                        queryRunnable?.run()
                    }
                    return true
                }
            })
    }

    override fun onDestroy() {
        queryCoroutine?.cancel()
        queryCoroutine = null
        queryRunnable = null
        productsAdapter = null
        super.onDestroy()
    }

    private fun getAndSubmitResults(flow: Flow<PagingData<Product>>) {

        vm.changeUiState(SUCCESS_KEYWORD)

        CoroutineScope(Dispatchers.IO).launch {
            flow.cachedIn(this).catch {
                vm.changeUiState(ERROR_KEYWORD)
            }.collect {
                vm.pagingData.postValue(it)
                productsAdapter!!.submitData(it)
            }
        }
    }

    private fun hideSoftKeyboard() {
        val v: View? = requireActivity().currentFocus
        val inputMethodManager = requireActivity().getSystemService(InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(
            v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onClick(item: Product) {
        vm.currentItem.postValue(item)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_fragment_container, ProductInfoFragment())
            .addToBackStack("new ProductInfoFragment added")
            .setReorderingAllowed(true)
            .commit()
    }
}
