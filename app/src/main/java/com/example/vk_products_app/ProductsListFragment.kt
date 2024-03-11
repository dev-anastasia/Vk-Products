package com.example.vk_products_app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vk_products_app.SearchViewModel.Companion.SKIP
import com.example.vk_products_app.paging.ProductsRxAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsListFragment : Fragment(R.layout.fragment_products_list) {

    private val vm: SearchViewModel by activityViewModels { SearchViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_products_list)
        recyclerView.layoutManager = GridLayoutManager(
            activity, 2
        )
        val productsAdapter = ProductsRxAdapter()
        recyclerView.adapter = productsAdapter

//        vm.searchUiState.observe(viewLifecycleOwner) { it ->
//            when (it) {
//
//                SearchUIState.Success -> {
//                    Log.d("TAG", "Success")
//                    productsAdapter.updateList(vm.productsList)
//                    productsAdapter.notifyDataSetChanged()
//                    //productsAdapter.submitData(it)
//                }
//
//                SearchUIState.NoResults -> {
//                    Toast.makeText(
//                        activity, "Результатов не найдено", Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                SearchUIState.Error -> {
//                    Toast.makeText(
//                        activity, "Непредвиденная ошибка", Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                else -> {}
//            }

        CoroutineScope(Dispatchers.IO).launch {
            vm.getProductsList().collect {
                productsAdapter.submitData(it)
            }
        }
    }
}
