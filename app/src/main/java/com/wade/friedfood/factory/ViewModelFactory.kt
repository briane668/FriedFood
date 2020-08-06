package com.wade.friedfood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wade.friedfood.login.LoginViewModel
import com.wade.friedfood.data.source.PublisherRepository

import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.profile.ProfileViewModel
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

                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)


                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
