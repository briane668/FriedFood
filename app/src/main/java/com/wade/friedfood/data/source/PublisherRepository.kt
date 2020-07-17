package com.wade.friedfood.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Menu
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Interface to the Publisher layers.
 */
interface PublisherRepository {



    suspend fun getShop(): Result<List<Shop>>

    suspend fun getComments(shop: Shop): Result<List<Comment>>

    suspend fun getFriedChicken(): Result<List<Menu>>

    suspend fun getSelectedShop(menus: List<Menu>): Result<List<Shop>>


}
