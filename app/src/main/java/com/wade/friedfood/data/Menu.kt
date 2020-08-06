package com.wade.friedfood.data

import kotlinx.android.parcel.RawValue
import java.util.*

data class Menu(
    val venderId:String ="",
    val name: String? = null,
    val price:Int =0
){
}
