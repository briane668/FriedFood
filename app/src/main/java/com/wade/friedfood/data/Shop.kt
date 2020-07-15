package com.wade.friedfood.data

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
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
    val recommend:String="",
    val star:String="",
    val address:String="",
    val menuImage:String ="",
    var otherImage:List<String> = listOf(),
    val location: @RawValue GeoPoint? = null,
    val comment: @RawValue List<Comment> = listOf()

    ) : Parcelable