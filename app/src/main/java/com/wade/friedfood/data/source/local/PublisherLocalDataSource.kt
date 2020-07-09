package app.appworks.school.publisher.data.source.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.data.Result
import app.appworks.school.publisher.data.source.PublisherDataSource
import com.wade.friedfood.data.Shop

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation of a Publisher source as a db.
 */
class PublisherLocalDataSource(val context: Context) : PublisherDataSource {



    override suspend fun getShop(): Result<List<Shop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
