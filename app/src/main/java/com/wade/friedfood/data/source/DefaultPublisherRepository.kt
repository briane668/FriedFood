package com.wade.friedfood.data.source

import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Menu
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop
import com.wade.friedfood.data.source.PublisherDataSource
import com.wade.friedfood.data.source.PublisherRepository

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultPublisherRepository(private val remoteDataSource: PublisherDataSource,
                                 private val localDataSource: PublisherDataSource
) : PublisherRepository {


    override suspend fun getShop(): Result<List<Shop>> {
        return remoteDataSource.getShops()
    }

    override suspend fun getComments(shop: Shop): Result<List<Comment>> {
        return remoteDataSource.getComments(shop)
    }

    override suspend fun getFriedChicken(): Result<List<Menu>> {
        return remoteDataSource.getFriedChicken()
    }

    override suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>> {
        return remoteDataSource.getSelectedShop(menus)
    }


}
