package com.wade.friedfood.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Wayne Chen on 2020-01-15.
 */
@Parcelize
data class Shop(
    val id: String = "",
    val name: String = "",
    val latitude: Int? = null,
    val longitude : Int? = null,
    val image:String ="",
    val recommend:String="",
    val star:String="",
    val address:String="",
    val menuImage:String ="",
    var otherImage:List<String> = listOf()

    ) : Parcelable