package com.wade.friedfood.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val user_id:String="",
    val name:String="",
    val picture:String="",
    val rating: Int = 0,
    val comment:String=""
): Parcelable