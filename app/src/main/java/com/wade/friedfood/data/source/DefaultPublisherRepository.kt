package com.wade.friedfood.data.source

import com.wade.friedfood.data.*


class DefaultPublisherRepository(private val remoteDataSource: PublisherDataSource,
                                 private val localDataSource: PublisherDataSource
) : PublisherRepository {


    override suspend fun getShops(): Result<List<Shop>> {
        return remoteDataSource.getShops()
    }

    override suspend fun getComments(id: String): Result<List<Comment>> {
        return remoteDataSource.getComments(id)
    }

    override suspend fun getMenu(food:String): Result<List<Menu>> {
        return remoteDataSource.searchFood(food)
    }

    override suspend fun searchShopByMenu(menus: List<Menu>): Result<List<Shop>> {
        return remoteDataSource.searchShopByMenu(menus)
    }

    override suspend fun getHowManyComments(shop: Shop): Result<Int> {
        return remoteDataSource.getHowManyComments(shop)
    }

    override suspend fun  getRating(shop: Shop): Result<Int> {
        return remoteDataSource.getRating(shop)
    }


    override suspend fun sendReview(shop: Shop,review: Review): Result<Int> {
        return remoteDataSource.sendReview(shop,review)
    }

    override suspend fun login(user: User): Result<Int> {
        return remoteDataSource.login(user)
    }

    override suspend fun collectShop(user: User, shop: Shop): Result<Int> {
        return remoteDataSource.collectShop(user,shop)
    }


    override suspend fun getUserData(user: User): Result<User> {
        return remoteDataSource.getUserData(user)
    }

    override suspend fun sendRating(shop: Shop, rating: Int): Result<Int> {
        return remoteDataSource.sendRating(shop,rating)
    }

    override suspend fun getUserCommentsCount(user_id: String): Result<Int> {
        return remoteDataSource.getUserCommentsCount(user_id)
    }

    override suspend fun getShopMenu(shop: ParcelableShop): Result<List<Menu>> {
        return remoteDataSource.getShopMenu(shop)
    }


}
