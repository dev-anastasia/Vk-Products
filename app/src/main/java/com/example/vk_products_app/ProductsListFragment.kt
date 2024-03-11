package com.example.vk_products_app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.cachedIn
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.paging.ProductsRxAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch

class ProductsListFragment : Fragment(R.layout.fragment_products_list) {

    private lateinit var vm: SearchViewModel

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
        val productsAdapter = ProductsRxAdapter()
        recyclerView.adapter = productsAdapter

        // Запрос в сеть
        CoroutineScope(Dispatchers.IO).launch {
            if (vm.pagingData.value != null) {
                productsAdapter.submitData(vm.pagingData.value!!)
            } else {
                vm.getProductsList()
                    .cachedIn(this@launch)
                    .onEmpty {
                        Toast.makeText(
                            activity, "Результатов не найдено", Toast.LENGTH_SHORT
                        ).show()
                    }
                    .catch {
                        Toast.makeText(
                            activity, "Непредвиденная ошибка", Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", "$it")
                    }
                    .collect {
                        vm.pagingData.postValue(it)
                        productsAdapter.submitData(it)
                    }
            }
        }
    }
}
