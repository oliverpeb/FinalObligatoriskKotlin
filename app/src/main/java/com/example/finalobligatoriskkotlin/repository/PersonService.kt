package com.example.finalobligatoriskkotlin.repository

import com.example.finalobligatoriskkotlin.models.Person
import retrofit2.Call
import retrofit2.http.*

interface PersonService {
    @GET("persons")
    fun getAllPersons(): Call<List<Person>>


    @GET("persons")
    fun getPersonsByUserId(@Query("user_Id") userId: String): Call<List<Person>>

    @GET("persons/{personId}")
    fun getPersonById(@Path("personId") personId: Int): Call<Person>

    @POST("persons")
    fun savePerson(@Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>
}