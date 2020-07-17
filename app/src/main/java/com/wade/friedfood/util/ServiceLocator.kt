package app.appworks.school.publisher.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wade.friedfood.data.source.DefaultPublisherRepository
import com.wade.friedfood.data.source.PublisherDataSource
import com.wade.friedfood.data.source.PublisherRepository
import com.wade.friedfood.data.source.local.PublisherLocalDataSource
import com.wade.friedfood.data.source.remote.PublisherRemoteDataSource

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * A Service Locator for the [PublisherRepository].
 */
object ServiceLocator {



    @Volatile
    var repository: PublisherRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): PublisherRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): PublisherRepository {
        return DefaultPublisherRepository(
            PublisherRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): PublisherDataSource {
        return PublisherLocalDataSource(context)
    }

}