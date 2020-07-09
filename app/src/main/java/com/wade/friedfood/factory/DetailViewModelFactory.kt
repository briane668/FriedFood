package com.wade.friedfood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.PublisherRepository

import com.wade.friedfood.data.Shop
import com.wade.friedfood.detail.DetailViewModel

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Factory for all ViewModels which need [Product].
 */
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val repository: PublisherRepository,
    private val shop: Shop
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(repository, shop)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
