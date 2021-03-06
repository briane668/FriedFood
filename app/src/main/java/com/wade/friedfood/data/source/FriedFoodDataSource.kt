package com.wade.friedfood.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.*
import kotlin.coroutines.Continuation


interface FriedFoodDataSource {


    suspend fun getShops(): Result<List<Shop>>

    suspend fun getComments(id: String): Result<List<Comment>>

    suspend fun searchFood(food:String): Result<List<Menu>>

    suspend fun searchShopByMenu(menus: List<Menu>): Result<List<Shop>>

    suspend fun getHowManyComments(shop: Shop): Result<Int>

    suspend fun getRating(shop: Shop): Result<Int>

    suspend fun sendReview(shop: Shop,review: Review): Result<Int>

    suspend fun login(user: User): Result<Int>

    suspend fun collectShop(user: User,shop: Shop): Result<Int>

    suspend fun getUserData(user: User): Result<User>

    suspend fun sendRating(shop: Shop,rating: Int) :Result<Int>

    suspend fun getUserCommentsCount(user_id: String): Result<Int>

    suspend fun getShopMenu(shop:ParcelableShop): Result<List<Menu>>

    suspend fun getNews(today:Long): Result<List<Comment>>
}
