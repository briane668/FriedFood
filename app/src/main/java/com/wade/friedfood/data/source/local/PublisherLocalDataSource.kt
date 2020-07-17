package com.wade.friedfood.data.source.local

import android.content.Context
import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Menu
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.source.PublisherDataSource
import com.wade.friedfood.data.Shop

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class PublisherLocalDataSource(val context: Context) : PublisherDataSource {



    override suspend fun getShops(): Result<List<Shop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getComments(shop: Shop): Result<List<Comment>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFriedChicken(): Result<List<Menu>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>> {
        TODO("Not yet implemented")
    }

}
