package com.wade.friedfood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.activity.viewModels

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController


import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.wade.friedfood.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

     private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    private lateinit var binding: ActivityMainBinding


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.mapsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recommend -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.recommendFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_menu -> {

                findNavController(this,R.id.myNavHostFragment).navigate(R.id.profileFragment)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupBottomNav()



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }
    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
//        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
//        bindingBadge.lifecycleOwner = this
//        bindingBadge.viewModel = viewModel
    }


}
//val shop = data!!.filter {
//    it.name == "awesome"
//}
//for ((index, shop) in data.withIndex()) {
//    if (shop.name == "awesome") {
//
//        val position = index
//    }
//}
//for (shop in data) {
//    if (shop.name == "awesome") {
//
//        val position = data!!.indexOf(shop)
//    }
//}