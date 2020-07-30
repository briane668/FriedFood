package com.wade.friedfood.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


/**
 * Created by Wayne Chen on 2020-01-15.
 */
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

//        private var geoPoint: GeoPoint? = null
//
//     override fun writeToParcel(parcel: Parcel, i: Int) {
//        parcel.writeDouble(geoPoint!!.latitude)
//        parcel.writeDouble(geoPoint!!.longitude)
//    }
//
//    fun shop(`in`: Parcel) {
//        val lat = `in`.readDouble()
//        val lng = `in`.readDouble()
//        geoPoint = GeoPoint(lat, lng)
//    }


}

