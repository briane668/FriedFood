package com.wade.friedfood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.appworks.school.publisher.data.source.PublisherRepository

import com.wade.friedfood.MainViewModel
import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.recommend.RecommendViewModel

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: PublisherRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(RecommendViewModel::class.java) ->
                    RecommendViewModel(repository)

                isAssignableFrom(MapViewModel::class.java) ->
                    MapViewModel(repository)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
