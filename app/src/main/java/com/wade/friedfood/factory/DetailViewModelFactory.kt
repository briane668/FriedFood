package com.wade.friedfood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.data.source.FriedFoodRepository

import com.wade.friedfood.detail.DetailViewModel
import com.wade.friedfood.detail.review.ReviewViewModel


@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val repository: FriedFoodRepository,
    private val parcelableShop: ParcelableShop
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(repository, parcelableShop)

                isAssignableFrom(ReviewViewModel::class.java) ->
                    ReviewViewModel(repository,parcelableShop)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
