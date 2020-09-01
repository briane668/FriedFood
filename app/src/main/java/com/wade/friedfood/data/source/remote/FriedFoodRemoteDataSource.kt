package com.wade.friedfood.data.source.remote

import androidx.lifecycle.MutableLiveData
import com.wade.friedfood.util.Logger
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.*
import com.wade.friedfood.data.source.FriedFoodDataSource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FriedFoodRemoteDataSource : FriedFoodDataSource {


    override suspend fun getShops(): Result<List<Shop>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("vender")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Shop>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
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


    override suspend fun getComments(id: String): Result<List<Comment>> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection("vender").document(id).collection("comment")
                .orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener { task ->
                    Logger.d("addOnCompleteListener, task=$task")
                    if (task.isSuccessful) {
                        val list = mutableListOf<Comment>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)
                            val comment = document.toObject(Comment::class.java)
                            list.add(comment)
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

    override suspend fun searchFood(food: String): Result<List<Menu>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("menu").whereEqualTo("name", food)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Menu>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " menu=> " + document.data)
                            val menu = document.toObject(Menu::class.java)
                            list.add(menu)
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


    override suspend fun searchShopByMenu(menus: List<Menu>): Result<List<Shop>> =
        suspendCoroutine { continuation ->
            val menusId: MutableList<String?> = mutableListOf()
            for (i in menus) {
                menusId += i.venderId
            }
            FirebaseFirestore.getInstance()
                .collection("vender").whereIn("id", menusId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Shop>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)
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


    override suspend fun getHowManyComments(shop: Shop): Result<Int> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("vender").document(shop.id).collection("comment")
                .get()
                .addOnCompleteListener { task ->
                    var count = 0
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Logger.d(document.id + " => " + document.data)
                            count++
                        }
                        continuation.resume(Result.Success(count))
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


    override suspend fun getRating(shop: Shop): Result<Int> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("vender").document(shop.id).collection("comment")
            .get()
            .addOnCompleteListener { task ->
                var rating = 0
                var count = 0
                var averageRating: Int
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Logger.d(document.id + " => " + document.data)
                        count++

                        val comment = document.toObject(Comment::class.java)

                        rating += comment.rating

                    }
                    averageRating = (rating / count)
                    continuation.resume(Result.Success(averageRating))
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


    override suspend fun sendReview(shop: Shop, review: Review): Result<Int> =
        suspendCoroutine { continuation ->
            val comment = FirebaseFirestore
                .getInstance()
                .collection("vender")
                .document(shop.id)
                .collection("comment")
                .document()


            comment.set(review)
            continuation.resume(Result.Success(1))
        }


    override suspend fun login(user: User): Result<Int> = suspendCoroutine { continuation ->
        val documents = FirebaseFirestore
            .getInstance()
            .collection("users")

        documents
            .whereEqualTo("id", user.id)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    documents
                        .document().set(user)
                } else {
                    Logger.d("已經是會員")
                }
            }

        continuation.resume(Result.Success(1))
    }

    // one obstacle one whereequalto
    override suspend fun collectShop(user: User, shop: Shop): Result<Int> =
        suspendCoroutine { continuation ->
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .whereEqualTo("id", user.id).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val collect = FirebaseFirestore
                                .getInstance().collection("users").document(document.id)
                            collect.update("collect", FieldValue.arrayUnion(shop))

                        }
                        continuation.resume(Result.Success(1))
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


    override suspend fun getUserData(user: User): Result<User> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("id", user.id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userData = MutableLiveData<User>()
                    for (document in task.result!!) {
                        Logger.d(document.id + " getUserData=> " + document.data)
                        val userDatas = document.toObject(User::class.java)
                        userData.apply {
                            value = userDatas
                        }
                    }

                    continuation.resume(Result.Success(userData.value!!))
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


    override suspend fun sendRating(shop: Shop, rating: Int): Result<Int> =
        suspendCoroutine { continuation ->
            val comment = FirebaseFirestore
                .getInstance()
                .collection("vender")
                .document(shop.id)

            comment.update("star", rating)
            continuation.resume(Result.Success(1))
        }


    override suspend fun getUserCommentsCount(user_id: String): Result<Int> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collectionGroup("comment")
                .whereEqualTo("user_id", user_id)
                .get()
                .addOnCompleteListener { task ->
                    var count = 0
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Logger.d(document.id + " getUserCommentsCount=> " + document.data)
                            count++
                        }
                        continuation.resume(Result.Success(count))
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


    override suspend fun getShopMenu(shop: ParcelableShop): Result<List<Menu>> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection("menu").whereEqualTo("venderId", shop.id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Menu>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " menu=> " + document.data)
                            val menu = document.toObject(Menu::class.java)
                            list.add(menu)
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

    override suspend fun getNews(today: Long): Result<List<Comment>> =
        suspendCoroutine { continuation ->
            val commentByWeek = today - 60480000
            FirebaseFirestore.getInstance()
                .collectionGroup("comment")
                .whereGreaterThanOrEqualTo("time", commentByWeek)
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Comment>()
                        for (document in task.result!!) {
                            Logger.d(document.id + " getNews=> " + document.data)
                            val comment = document.toObject(Comment::class.java)
                            list.add(comment)
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





