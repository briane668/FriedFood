package com.wade.friedfood.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.*

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Interface to the Publisher layers.
 */
interface PublisherRepository {


    suspend fun getShops(): Result<List<Shop>>

    suspend fun getComments(shop: Shop): Result<List<Comment>>

    suspend fun getMenu(food:String): Result<List<Menu>>

    suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>>

    suspend fun getHowManyComments(shop: Shop): Result<Int>

    suspend fun getRating(shop: Shop): Result<Int>

    suspend fun sendReview(shop: Shop,review: Review): Result<Int>

    suspend fun login(user: User): Result<Int>

    suspend fun collectShop(user: User,shop: Shop): Result<Int>

    suspend fun getUserData(user: User): Result<User>

    suspend fun sendRating(shop: Shop,rating: Int) :Result<Int>
}
