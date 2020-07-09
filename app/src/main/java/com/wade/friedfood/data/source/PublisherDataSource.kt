package app.appworks.school.publisher.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.Shop
import com.wade.friedfood.data.Result
import kotlin.coroutines.Continuation

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Main entry point for accessing Publisher sources.
 */
interface PublisherDataSource {


    suspend fun getShop(): Result<List<Shop>>


}
