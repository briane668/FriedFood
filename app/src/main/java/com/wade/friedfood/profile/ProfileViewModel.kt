package com.wade.friedfood.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wade.friedfood.data.User
import com.wade.friedfood.util.UserManager

class ProfileViewModel : ViewModel() {

    private val _ProfileData :LiveData<User> = UserManager.ProfileData

    val ProfileData: LiveData<User>
        get() = _ProfileData

    // TODO: Implement the ViewModel





}
