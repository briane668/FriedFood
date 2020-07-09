package app.appworks.school.publisher.data.source

import androidx.lifecycle.MutableLiveData
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


    override suspend fun getShop(): Result<List<Shop>> {
        return remoteDataSource.getShop()
    }

}
