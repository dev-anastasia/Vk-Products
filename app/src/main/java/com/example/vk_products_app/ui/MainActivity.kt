package com.example.vk_products_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vk_products_app.R
import com.example.vk_products_app.ui.fragments.ProductsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_activity_fragment_container, ProductsListFragment())
                .setReorderingAllowed(true)
                .commit()
        }
    }
}