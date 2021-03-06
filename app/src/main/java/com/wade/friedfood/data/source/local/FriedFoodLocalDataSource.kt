package com.wade.friedfood.data.source.local

import android.content.Context
import com.wade.friedfood.data.*
import com.wade.friedfood.data.source.FriedFoodDataSource


class FriedFoodLocalDataSource(val context: Context) : FriedFoodDataSource {



    override suspend fun getShops(): Result<List<Shop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getComments( id: String): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchFood(food:String): Result<List<Menu>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchShopByMenu(menus: List<Menu>): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHowManyComments(shop: Shop): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getRating(shop: Shop): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun sendReview(shop: Shop, review: Review): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun login(user: User): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun collectShop(user: User, shop: Shop): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserData(user: User): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRating(shop: Shop, rating: Int): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserCommentsCount(user_id: String): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getShopMenu(shop:ParcelableShop): Result<List<Menu>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNews(today: Long): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

}
