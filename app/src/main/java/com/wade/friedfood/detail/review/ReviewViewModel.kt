package com.wade.friedfood.detail.review


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.PublisherRepository
import app.appworks.school.publisher.network.LoadApiStatus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class ReviewViewModel(private val stylishRepository: PublisherRepository) : ViewModel() {

    val rating = MutableLiveData<Int>().apply {
        value = 0
    }

//    val user = MutableLiveData<User>().apply {
//        value = userArg
//    }



    val comment = MutableLiveData<String>()




    private val _reviewFinish = MutableLiveData<Boolean>()
    val reviewFinish: LiveData<Boolean>
        get() = _reviewFinish

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
















}