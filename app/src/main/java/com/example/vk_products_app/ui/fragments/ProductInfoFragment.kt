package com.example.vk_products_app.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.vk_products_app.R
import com.example.vk_products_app.ui.ProductsViewModel
import com.example.vk_products_app.ui.imagesAdapter.ImagesSwipeAdapter

class ProductInfoFragment : Fragment(R.layout.fragment_product_info_page) {

    private lateinit var vm: ProductsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        vm = ViewModelProvider(
            requireActivity(), ProductsViewModel.Factory
        )[ProductsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireView().findViewById<TextView>(R.id.tv_product_title_product_info_fr).text =
            vm.currentItem.value?.title
        requireView().findViewById<TextView>(R.id.tv_brand_name).text =
            vm.currentItem.value?.brand
        requireView().findViewById<TextView>(R.id.tv_product_price).text =
            vm.currentItem.value?.price.toString()
        requireView().findViewById<TextView>(R.id.tv_product_rating).text =
            vm.currentItem.value?.rating.toString()
        requireView().findViewById<TextView>(R.id.tv_description_product_info_fr).text =
            vm.currentItem.value?.description

        val imageViewPager =
            requireView().findViewById<ViewPager2>(R.id.vp_product_image_view_pager)
        imageViewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = ImagesSwipeAdapter(vm.currentItem.value?.images ?: emptyList())
        }

        requireView().findViewById<ImageButton>(R.id.btn_go_back_product_info_fr)
            .setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
    }
}