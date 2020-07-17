package com.wade.friedfood

import android.app.Application
import com.wade.friedfood.data.source.PublisherRepository
import app.appworks.school.publisher.util.ServiceLocator

import kotlin.properties.Delegates

/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class MyApplication : Application() {


    val repository: PublisherRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var INSTANCE: MyApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun isLiveDataDesign() = true
}
