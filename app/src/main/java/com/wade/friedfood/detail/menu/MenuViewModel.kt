package com.wade.friedfood.detail.menu

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


class MenuViewModel(private val repository: PublisherRepository,
                      shop: ParcelableShop
) : ViewModel() {





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










}