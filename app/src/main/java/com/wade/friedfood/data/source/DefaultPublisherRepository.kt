package com.wade.friedfood.data.source

import com.wade.friedfood.data.*

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultPublisherRepository(private val remoteDataSource: PublisherDataSource,
                                 private val localDataSource: PublisherDataSource
) : PublisherRepository {


    override suspend fun getShops(): Result<List<Shop>> {
        return remoteDataSource.getShops()
    }

    override suspend fun getComments(shop: Shop): Result<List<Comment>> {
        return remoteDataSource.getComments(shop)
    }

    override suspend fun getMenu(food:String): Result<List<Menu>> {
        return remoteDataSource.getMenu(food)
    }

    override suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>> {
        return remoteDataSource.getSelectedShop(menus)
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



}
