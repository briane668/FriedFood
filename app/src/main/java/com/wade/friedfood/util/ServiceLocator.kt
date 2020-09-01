package app.appworks.school.publisher.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wade.friedfood.data.source.DefaultFriedFoodRepository
import com.wade.friedfood.data.source.FriedFoodDataSource
import com.wade.friedfood.data.source.FriedFoodRepository
import com.wade.friedfood.data.source.local.FriedFoodLocalDataSource
import com.wade.friedfood.data.source.remote.FriedFoodRemoteDataSource


object ServiceLocator {



    @Volatile
    var repository: FriedFoodRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): FriedFoodRepository {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): FriedFoodRepository {
        return DefaultFriedFoodRepository(
            FriedFoodRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): FriedFoodDataSource {
        return FriedFoodLocalDataSource(context)
    }

}