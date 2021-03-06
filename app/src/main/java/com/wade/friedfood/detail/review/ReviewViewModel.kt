package com.wade.friedfood.detail.review


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.ext.toShop
import com.wade.friedfood.data.source.FriedFoodRepository
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.*
import com.wade.friedfood.util.UserManager.ProfileData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class ReviewViewModel(
    private val repository: FriedFoodRepository,
    shop: ParcelableShop
) : ViewModel() {

    val rating = MutableLiveData<Int>().apply {
        value = 1
    }


    val comment = MutableLiveData<String>()


    private val _menu = MutableLiveData<List<Menu>>()


    val menu: LiveData<List<Menu>>
        get() = _menu


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


    fun prepareSendReview(image: String) {
        val review = Review(
            user_id = ProfileData.id,
            name = ProfileData.name,
            picture = ProfileData.picture,
            rating = rating.value!!,
            comment = comment.value!!,
            time = Calendar.getInstance()
                .timeInMillis,
            image = image,
            shop = shop.value!!.toShop()
        )

        shop.value?.let {
            sendReview(it.toShop(), review)
        }

    }


    init {

        getShopMenu(shop)

    }


    private fun sendReview(shop: Shop, review: Review) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.sendReview(shop, review)

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


    private fun getShopMenu(shop: ParcelableShop) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShopMenu(shop)

            _menu.value = when (result) {
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