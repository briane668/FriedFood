package com.wade.friedfood.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    val user_id:String,
    val name:String,
    val picture:String,
    val rating:Int,
    val comment:String,
    val time: Long,
    val image:String,
    val shop:Shop

): Parcelable