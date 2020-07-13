package com.wade.friedfood.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.PublisherRepository
import app.appworks.school.publisher.network.LoadApiStatus
import app.appworks.school.publisher.util.Logger
import app.appworks.school.publisher.util.ServiceLocator.repository
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapViewModel (private val repository: PublisherRepository)  :ViewModel(){




 val shop = MutableLiveData<List<Shop>>()

//
//    val shop: LiveData<List<Shop>>
//        get() = _shop



    var selectedShop = MutableLiveData<String>()

    var Position = MutableLiveData<Int>()

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
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


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


    fun getShop() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShop()

            shop.value = when (result) {
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