package com.example.vk_products_app.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.vk_products_app.ProductsListFragment.Companion.KEY
import com.example.vk_products_app.R
import com.example.vk_products_app.SearchViewModel
import com.example.vk_products_app.adapter.ImagesSwipeAdapter
import com.example.vk_products_app.entities.Product

class ProductInfoFragment : Fragment(R.layout.fragment_product_info_page) {

    private lateinit var vm: SearchViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        vm = ViewModelProvider(
            requireActivity(), SearchViewModel.Factory
        )[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = this.arguments?.getParcelable<Product>(KEY)

        requireView().findViewById<TextView>(R.id.tv_product_title_product_info_fr).text =
            product?.title
        requireView().findViewById<TextView>(R.id.tv_brand_name).text =
            product?.brand
        requireView().findViewById<TextView>(R.id.tv_product_price).text =
            product?.price.toString()
        requireView().findViewById<TextView>(R.id.tv_product_rating).text =
            product?.rating.toString()
        requireView().findViewById<TextView>(R.id.tv_description_product_info_fr).text =
            product?.description

        val imageViewPager =
            requireView().findViewById<ViewPager2>(R.id.vp_product_image_view_pager)
        imageViewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = ImagesSwipeAdapter(product?.images ?: emptyList())
        }

        requireView().findViewById<ImageButton>(R.id.btn_go_back_product_info_fr)
            .setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy")
    }
}