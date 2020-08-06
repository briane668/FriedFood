package com.wade.friedfood.map

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.wade.friedfood.data.source.PublisherRepository
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.util.Logger
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Menu
import com.wade.friedfood.data.ParcelableShop
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop
import com.wade.friedfood.getDistance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MapViewModel (private val repository: PublisherRepository)  :ViewModel(){




    val shop = MutableLiveData<List<Shop>>()

    val naerShop= MutableLiveData<List<Shop>>()


    val menus = MutableLiveData<List<Menu>>()

    val howManyComments  = MutableLiveData<Int>()


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

    companion object {
        val userPosition = MutableLiveData<Location>()

    }



    init {
        Logger.i("------------------------------------")
        Logger.i("[${this::class.simpleName}]${this}")
        Logger.i("------------------------------------")


        getShops()




    }


     fun getShops() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShops()

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





     fun getMenu(food:String) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING


            val result = repository.getMenu(food)

            menus.value = when (result) {
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


    fun searchShopByMenu(menus:List<Menu>) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.searchShopByMenu(menus)

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





    fun calculateDistance(shops:List<Shop>){

        val list = mutableListOf<Shop>()

        for (shop in shops){
            val x = userPosition.value?.latitude
            val y = userPosition.value?.longitude
            val r = shop.location?.latitude
            val s = shop.location?.longitude
            val m= getDistance(x ?: 0.toDouble(), y ?: 0.toDouble(), r ?: 0.toDouble(), s ?: 0.toDouble())
            val k = m.roundToInt()
            if ( k <10000000 ){
                list.add(shop)
            }
        }
        naerShop.value =list
    }




}