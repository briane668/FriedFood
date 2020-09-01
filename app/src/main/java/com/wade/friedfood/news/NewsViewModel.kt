package com.wade.friedfood.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.MyApplication
import com.wade.friedfood.R
import com.wade.friedfood.data.Comment
import com.wade.friedfood.data.Result
import com.wade.friedfood.data.Shop
import com.wade.friedfood.data.source.FriedFoodRepository
import com.wade.friedfood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import java.util.*

class NewsViewModel(private val repository: FriedFoodRepository) : ViewModel() {



    val _comments = MutableLiveData<List<Comment>>()

    val comments: LiveData<List<Comment>>
        get() = _comments

    val todayTime = Calendar.getInstance()
        .timeInMillis

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _refreshStatus = MutableLiveData<Boolean>()

    val refreshStatus: LiveData<Boolean>
        get() = _refreshStatus

    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedShop = MutableLiveData<Comment>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedShop: LiveData<Comment>
        get() = _navigateToSelectedShop

    fun displayShopDetails(comment: Comment) {
        _navigateToSelectedShop.value = comment
    }

    fun displayShopDetailsComplete() {
        _navigateToSelectedShop.value = null
    }



    init {

        getNews(todayTime)


    }



    private fun getNews(todayTime:Long) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            val result = repository.getNews(todayTime)

            _comments.value = when (result) {
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



    suspend fun getUserCommentsCount(user_id: String): Int {


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

}
