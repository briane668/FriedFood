package com.wade.friedfood.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.data.source.PublisherRepository
import com.wade.friedfood.network.LoadApiStatus
import com.wade.friedfood.util.Logger
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Shop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.wade.friedfood.data.Result



class RecommendViewModel(private val repository: PublisherRepository) : ViewModel() {


//    只用在最開始傳進來的shops值
    val _shops = MutableLiveData<List<Shop>>()



//    用來裝之後每次篩選的shop值操作，真正拿來用的shops
    private val readyShops = MutableLiveData<List<Shop>>()

    val shops: LiveData<List<Shop>>
        get() = readyShops

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

            getShops()

    }


    private fun getShops() {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getShops()

            _shops.value = when (result) {
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

    suspend fun shopAddComments(shop:Shop): Int {

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


    suspend fun shopAddRating(shop:Shop): Int {

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


//    兩個排序的function

    fun sortShopByRate(shop: LiveData<List<Shop>>){
        readyShops.value = shop.value?.sortedByDescending {
            it.star
        }
    }

    fun sortShopByComment(shop: LiveData<List<Shop>>){
        readyShops.value = shop.value?.sortedByDescending {
            it.recommend
        }
    }



//原本的comments沒有評價和評論數，在這裡把他們加上去
    fun addRatingToComments(shops: MutableLiveData<List<Shop>>){
val counts = 0
//    TODO: 用count來操作?
        for (shop in shops.value!!){
            coroutineScope.launch {
                shop.recommend =shopAddComments(shop)
            }
            coroutineScope.launch {
                shop.star =shopAddRating(shop)
            }
        }
    readyShops.value = shops.value

    }



}