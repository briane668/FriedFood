package com.wade.friedfood.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.data.source.PublisherRepository
import com.wade.friedfood.data.Shop

class DetailViewModel(
    repository: PublisherRepository,
    shop: Shop
) : ViewModel() {


    private val _shop = MutableLiveData<Shop>().apply {
        value = shop
    }

    val shop: LiveData<Shop>
        get() = _shop









}
