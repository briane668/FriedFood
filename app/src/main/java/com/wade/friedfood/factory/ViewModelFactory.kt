package com.wade.friedfood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wade.friedfood.MainViewModel
import com.wade.friedfood.login.LoginViewModel
import com.wade.friedfood.data.source.FriedFoodRepository

import com.wade.friedfood.map.MapViewModel
import com.wade.friedfood.news.NewsViewModel
import com.wade.friedfood.profile.ProfileViewModel
import com.wade.friedfood.recommend.RecommendViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: FriedFoodRepository
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

                isAssignableFrom(NewsViewModel::class.java) ->
                    NewsViewModel(repository)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
