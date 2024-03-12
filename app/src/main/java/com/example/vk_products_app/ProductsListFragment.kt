package com.example.vk_products_app

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
import com.example.vk_products_app.SearchViewModel.Companion.ERROR
import com.example.vk_products_app.SearchViewModel.Companion.NO_RESULTS
import com.example.vk_products_app.SearchViewModel.Companion.SKIP
import com.example.vk_products_app.SearchViewModel.Companion.SUCCESS
import com.example.vk_products_app.entities.Product
import com.example.vk_products_app.paging.ProductsRxAdapter
import com.example.vk_products_app.ui.ItemClickListener
import com.example.vk_products_app.ui.ProductInfoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

class ProductsListFragment : Fragment(R.layout.fragment_products_list), ItemClickListener {

    private lateinit var vm: SearchViewModel
    private var queryRunnable: Runnable? = null
    private var queryCoroutine: Job? = null
    private var productsAdapter: ProductsRxAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        vm = ViewModelProvider(
            requireActivity(), SearchViewModel.Factory
        )[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_products_list)
        recyclerView.layoutManager = GridLayoutManager(
            activity, 2,
        )
        productsAdapter = ProductsRxAdapter(this)
        recyclerView.adapter = productsAdapter

        // Observers
        val loadingIcon = requireView().findViewById<ImageView>(R.id.iv_loading)
        vm.searchUiState.observe(viewLifecycleOwner) {
            when (it) {
                SearchUIState.Loading -> {
                    hideSoftKeyboard()
                    loadingIcon.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }

                SearchUIState.NoResults -> {
                    loadingIcon.visibility = View.GONE
                    Toast.makeText(
                        activity, "Результатов не найдено", Toast.LENGTH_SHORT
                    ).show()
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

                else -> {}
            }
        }

        // Запрос в сеть: получение списка товаров
        CoroutineScope(Dispatchers.IO).launch {
            if (vm.pagingData.value != null) {
                productsAdapter!!.submitData(vm.pagingData.value!!)
                vm.changeUiState(SUCCESS)
            } else {
                val flow = vm.getProductsList()
                getAndSubmitResults(flow)
            }
        }

        // Запрос в сеть: поисковик
        queryRunnable = Runnable {
            if (CURRENT_QUERY.isEmpty().not()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val flow = vm.getProductsList()
                    getAndSubmitResults(flow)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        requireView().findViewById<SearchView>(R.id.sv_search).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    CURRENT_QUERY = query ?: ""
                    queryCoroutine?.cancel()
                    queryCoroutine = CoroutineScope(Dispatchers.IO).launch {
                        SKIP = 0
                        queryRunnable?.run()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    CURRENT_QUERY = newText ?: ""
                    queryCoroutine?.cancel()
                    queryCoroutine = CoroutineScope(Dispatchers.IO).launch {
                        delay(DELAY_TIMER)
                        SKIP = 0
                        queryRunnable?.run()
                    }
                    return true
                }
            }
        )
    }

    override fun onDestroy() {
        queryRunnable = null
        queryCoroutine?.cancel()
        queryCoroutine = null
        productsAdapter = null
        super.onDestroy()
    }

    private fun getAndSubmitResults(flow: Flow<PagingData<Product>>) {
        CoroutineScope(Dispatchers.IO).launch {
            flow
                .cachedIn(this)
                .onEmpty {
                    vm.changeUiState(NO_RESULTS)
                }
                .catch {
                    vm.changeUiState(ERROR)
                }
                .onCompletion {
                    queryRunnable = null
                    queryCoroutine?.cancel()
                }
                .collect {
                    vm.pagingData.postValue(it)
                    vm.changeUiState(SUCCESS)
                    productsAdapter!!.submitData(it)
                }
        }
    }

    private fun hideSoftKeyboard() {
        val v: View? = requireActivity().currentFocus
        val inputMethodManager =
            requireActivity().getSystemService(InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(
            v?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onClick(item: Product) {
        val productInfoFr = ProductInfoFragment()
        val bundle = Bundle()
        bundle.putParcelable(KEY, item)
        productInfoFr.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_fragment_container, productInfoFr)
            .addToBackStack("new ProductInfoFragment added")
            .setReorderingAllowed(true)
            .commit()
    }

    companion object {
        var CURRENT_QUERY: String = ""
        const val KEY = "KEY"
        private const val DELAY_TIMER = 2000L
    }
}
