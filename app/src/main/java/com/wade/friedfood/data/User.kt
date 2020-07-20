package com.wade.friedfood.data

data class User
    (
    var id: Long,
    var provider:String,
    var name:String,
    var email:String,
    var picture: String
)