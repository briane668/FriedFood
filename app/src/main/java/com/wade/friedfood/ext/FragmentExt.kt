package com.wade.friedfood.ext

import androidx.fragment.app.Fragment
import com.wade.friedfood.MyApplication
import com.wade.friedfood.data.Shop
import com.wade.friedfood.factory.DetailViewModelFactory
import com.wade.friedfood.factory.ViewModelFactory


/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(shop: Shop): DetailViewModelFactory {
    val repository = (requireContext().applicationContext as MyApplication).repository
    return DetailViewModelFactory(repository,shop)
}