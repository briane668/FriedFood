package com.wade.friedfood.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.util.Logger
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.*
import com.wade.friedfood.data.source.PublisherRepository
import com.wade.friedfood.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DetailViewModel(
//    repository在底下的    val result = repository.getComments(shop) 用到
    private val repository: PublisherRepository,
    shop: ParcelableShop
) : ViewModel() {


//這邊的寫法

    private val _shop = MutableLiveData<ParcelableShop>().apply {
        value = shop
    }

    val _userData = MutableLiveData<User>()


    val shop: LiveData<ParcelableShop>
        get() = _shop


    private val _comment = MutableLiveData<List<Comment>>()


    val comment: LiveData<List<Comment>>
        get() = _comment


    private val _sortedComment = MutableLiveData<List<Comment>>()

    val sortedComment: LiveData<List<Comment>>
        get() = _sortedComment

    val collectDone = MutableLiveData<Int>()


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // status: The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private val _alreadyCollectShop = MutableLiveData<Boolean>().apply {
        value = null
    }

    val alreadyCollectShop: LiveData<Boolean>
        get() = _alreadyCollectShop



//    var alreadyCollectShop: Boolean? =null






    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")


        getUserData(UserManager.ProfileData)

        getComments(shop)

    }

    fun collectAble(user: User){

        for (thisShop in user.collect){
            if (thisShop.id == shop.value?.id){
                _alreadyCollectShop.value= true
                break

            }else{
                _alreadyCollectShop.value=false
            }

        }





    }




    fun getComments(shop: ParcelableShop) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getComments(shop.id)

            _comment.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }


    suspend fun getCommentsByShop(shop: Shop): Int {

        val result = repository.getHowManyComments(shop)

        return when (result) {
            is Result.Success -> {
                _error.value = null
                _status.value = LoadApiStatus.DONE
                result.data
            }
            is Result.Fail -> {
                _error.value = result.error
                _status.value = LoadApiStatus.ERROR
                0
            }
            is Result.Error -> {
                _error.value = result.exception.toString()
                _status.value = LoadApiStatus.ERROR
                0
            }
            else -> {
                _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                _status.value = LoadApiStatus.ERROR
                0
            }
        }
    }


    suspend fun getRatingByShop(shop: Shop): Int {

        val result = repository.getRating(shop)

        return when (result) {
            is Result.Success -> {
                _error.value = null
                _status.value = LoadApiStatus.DONE
                result.data
            }
            is Result.Fail -> {
                _error.value = result.error
                _status.value = LoadApiStatus.ERROR
                0
            }
            is Result.Error -> {
                _error.value = result.exception.toString()
                _status.value = LoadApiStatus.ERROR
                0
            }
            else -> {
                _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                _status.value = LoadApiStatus.ERROR
                0
            }
        }
    }


    fun collectShop(user: User, shop: Shop) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.collectShop(user, shop)

            collectDone.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }


    fun sortComment(comment: List<Comment>) {


//        val sortedComment = comment.sortedByDescending { comment ->
//            comment.time
//        }
//        Log.d("CommentSOrt","$comment")
//        _sortedComment.value.apply {
//            sortedComment
//            Log.d("CommentSOrt","$sortedComment")
//        }


        _sortedComment.value = comment.sortedByDescending {
            it.time
        }

//        val list = comment.toMutableList()
//
//        Log.i("Sort Info","comments before sort")
//        list.forEach {
//            Log.i("Sort Info","comment=${it.time}")
//        }
//
//        list.sortBy {
//            it.time
//        }
//
//        Log.d("Sort Info","comments after sort")
//        list.forEach {
//            Log.d("Sort Info","comment=${it.time}")
//        }

    }

//    fun dialPhoneNumber(phoneNumber: String) {
//        val intent = Intent(Intent.ACTION_DIAL).apply {
//            data = Uri.parse("tel:$phoneNumber")
//        }
//            startActivity(intent)
//    }
//

//    private var geoPoint: GeoPoint? = null
//
//     override fun writeToParcel(parcel: Parcel, i: Int) {
//        parcel.writeDouble(geoPoint!!.latitude)
//        parcel.writeDouble(geoPoint!!.longitude)
//    }
//
//    fun Restaurant(`in`: Parcel) {
//        val lat = `in`.readDouble()
//        val lng = `in`.readDouble()
//        geoPoint = GeoPoint(lat, lng)
//    }


    suspend fun getUserCommentsCount(user_id: String): Int {

        _status.value = LoadApiStatus.LOADING

        val result = repository.getUserCommentsCount(user_id)

        return when (result) {
            is Result.Success -> {
                _error.value = null
                _status.value = LoadApiStatus.DONE
                result.data
            }
            is Result.Fail -> {
                _error.value = result.error
                _status.value = LoadApiStatus.ERROR
                null
            }
            is Result.Error -> {
                _error.value = result.exception.toString()
                _status.value = LoadApiStatus.ERROR
                null
            }
            else -> {
                _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                _status.value = LoadApiStatus.ERROR
                null
            }

        } as Int
    }



    fun getUserData(user: User) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getUserData(user)

            _userData.value = when (result) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                    result.data
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                    null
                }
                else -> {
                    _error.value = MyApplication.INSTANCE.getString(R.string.you_know_nothing)
                    _status.value = LoadApiStatus.ERROR
                    null
                }
            }
            _refreshStatus.value = false
        }
    }




}
