package com.wade.friedfood.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.data.source.PublisherRepository
import app.appworks.school.publisher.network.LoadApiStatus
import app.appworks.school.publisher.util.Logger
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wade.friedfood.data.Result



class RecommendViewModel(private val repository: PublisherRepository) : ViewModel() {

    val _shop = MutableLiveData<List<Shop>>()

    private val readyShop = MutableLiveData<List<Shop>>()


    // The external LiveData interface to the property is immutable, so only this class can modify
    val shop: LiveData<List<Shop>>
        get() = readyShop

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

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedShop = MutableLiveData<Shop>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedShop: LiveData<Shop>
        get() = _navigateToSelectedShop

    fun displayShopDetails(shop: Shop) {
        _navigateToSelectedShop.value = shop
    }

    fun displayShopDetailsComplete() {
        _navigateToSelectedShop.value = null
    }




    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")


            getShop()

    }


    private fun getShop() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShops()

            _shop.value = when (result) {
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

    suspend fun getCommentsByShop(shop:Shop): Int {

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


    suspend fun getRatingByShop(shop:Shop): Int {

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


    fun sortShopByRate(shop: LiveData<List<Shop>>){

        _shop.value = shop.value?.sortedByDescending {
            it.star
        }
    }
    fun sortShopByComment(shop: LiveData<List<Shop>>){

        _shop.value = shop.value?.sortedByDescending {
            it.recommend
        }
    }




    fun addRatingComment(shops: MutableLiveData<List<Shop>>){

        for (shop in shops.value!!){
            coroutineScope.launch {
                shop.recommend =getCommentsByShop(shop)
            }
            coroutineScope.launch {
                shop.star =getRatingByShop(shop)
            }
        }
        readyShop.value = shops.value

    }



}