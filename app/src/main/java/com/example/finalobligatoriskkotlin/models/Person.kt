package com.example.finalobligatoriskkotlin.models

import com.squareup.moshi.Json

data class Person (
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "userId") val userId: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "birthYear") val birthYear: Int,
    @field:Json(name = "birthMonth") val birthMonth: Int,
    @field:Json(name = "birthDayOfMonth") val birthDayOfMonth: Int,
    @field:Json(name = "remarks") val remarks: String?,
    @field:Json(name = "pictureUrl") val pictureUrl: String?,
    @field:Json(name = "age") val age: Int
){
    override fun toString(): String{
        return name + "\n" + age
    }
}