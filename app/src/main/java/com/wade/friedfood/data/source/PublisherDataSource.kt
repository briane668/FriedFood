package com.wade.friedfood.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.*
import kotlin.coroutines.Continuation

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Main entry point for accessing Publisher sources.
 */
interface PublisherDataSource {


    suspend fun getShops(): Result<List<Shop>>

    suspend fun getComments(shop: Shop): Result<List<Comment>>

    suspend fun getMenu(food:String): Result<List<Menu>>

    suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>>

    suspend fun getHowManyComments(shop: Shop): Result<Int>

    suspend fun getRating(shop: Shop): Result<Int>

    suspend fun sendReview(shop: Shop,review: Review): Result<Int>

}
