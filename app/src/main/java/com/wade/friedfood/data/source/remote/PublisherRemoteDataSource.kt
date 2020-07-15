package com.wade.friedfood.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore

import app.appworks.school.publisher.data.source.PublisherDataSource
import app.appworks.school.publisher.util.Logger
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Shop
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.wade.friedfood.data.Result


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Implementation of the Publisher source that from network.
 */
object PublisherRemoteDataSource : PublisherDataSource {

//    private const val PATH_ARTICLES = "vender"
//    private const val KEY_CREATED_TIME = "createdTime"



    override suspend fun getShop(): Result<List<Shop>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("vender")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Shop>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
//有問題??

//Could not deserialize object. Class com.wade.friedfood.data.Shop does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped
                        val article = document.toObject(Shop::class.java)
                        list.add(article)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {

                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail(MyApplication.INSTANCE.getString(R.string.you_know_nothing)))
                }
            }
    }







}
