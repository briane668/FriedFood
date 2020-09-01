package com.wade.friedfood.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class Shop(
    val id: String = "",
    val name: String = "",
    val latitude: Int? = null,
    val longitude: Int? = null,
    val image:String ="",
    var recommend:Int=0,
    var star:Int=0,
    val address:String="",
    val menuImage:String ="",
    var otherImage:List<String> = listOf(),
    val location: @RawValue GeoPoint? = null,
    val comment: @RawValue List<Comment> = listOf(),
    val menu: @RawValue List<Food> = listOf(),
    val phone:String = ""

    ) : Parcelable{



}

