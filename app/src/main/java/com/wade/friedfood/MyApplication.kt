package com.wade.friedfood

import android.app.Application
import com.wade.friedfood.data.source.FriedFoodRepository
import app.appworks.school.publisher.util.ServiceLocator

import kotlin.properties.Delegates


class MyApplication : Application() {


    val repository: FriedFoodRepository
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
