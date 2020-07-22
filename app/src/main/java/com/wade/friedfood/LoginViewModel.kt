package com.wade.friedfood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.appworks.school.publisher.network.LoadApiStatus

import com.google.firebase.firestore.FirebaseFirestore
import com.wade.friedfood.data.User
import com.wade.friedfood.data.source.PublisherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class LoginViewModel (private val repository: PublisherRepository) : ViewModel(){




    var notAlreadySign=MutableLiveData<Boolean>().apply {
        value=false
    }



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



fun addAble (user: User){
    FirebaseFirestore
        .getInstance()
        .collection("users")
        .whereEqualTo("id",user.id)
        .get()
        .addOnSuccessListener {
//有東西的話  變成false   我希望他搜尋不到東西 代表可以加
            notAlreadySign.value=it.isEmpty
        }
        .addOnFailureListener {
            notAlreadySign.value= true
        }
}

     suspend fun login(user: User) {

        repository.login(user)

    }







}