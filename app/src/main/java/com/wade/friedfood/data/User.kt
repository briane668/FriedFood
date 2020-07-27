package com.wade.friedfood.data

import kotlinx.android.parcel.RawValue

data class User
    (
    var id: String = "",
    var name:String= "",
    var email:String= "",
    var picture: String="",
    var collect: @RawValue List<Shop> = listOf(),
    var howManyComments : Int =0
)