package com.wade.friedfood.detail.review


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.stylish.ext.toShop
import com.wade.friedfood.data.source.PublisherRepository
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Review
import com.wade.friedfood.data.Shop
import com.wade.friedfood.util.UserManager.ProfileData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class ReviewViewModel(private val repository: PublisherRepository,
                      shop: ParcelableShop
) : ViewModel() {

    val rating = MutableLiveData<Int>().apply {
        value = 0
    }

//    val user = MutableLiveData<User>().apply {
//        value = userArg
//    }

//    val whichShop = shop.id

    val comment = MutableLiveData<String>()





    var sendSuccess = MutableLiveData<Int>()


    private val _shop = MutableLiveData<ParcelableShop>().apply {
        value = shop
    }
    val shop: LiveData<ParcelableShop>
        get() = _shop







    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    // error: The internal MutableLiveData that stores the error of the most recent request
    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    // status for the loading icon of swl
    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus



    fun prepareSendReview(image:String) {
        val review = Review(
             user_id = ProfileData.id,
             name = ProfileData.name,
             picture = ProfileData.picture,
             rating = rating.value!!,
             comment =  comment.value!!,
            time = Calendar.getInstance()
                .timeInMillis,
            image =image
        )

        shop.value?.let {
            val shop =it.toShop()


            sendReview(shop,review) }

    }






    private fun sendReview(shop:Shop, review: Review) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.sendReview(shop,review )

            sendSuccess.value = when (result) {
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