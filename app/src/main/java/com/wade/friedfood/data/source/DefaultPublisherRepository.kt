package com.wade.friedfood.data.source

import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Menu
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop

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


}
