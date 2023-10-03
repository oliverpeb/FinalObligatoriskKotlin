package com.example.finalobligatoriskkotlin.repository

import androidx.lifecycle.MutableLiveData
import com.example.finalobligatoriskkotlin.models.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import java.util.Calendar



class PersonRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"

    private val personService: PersonService
    val personsLiveData: MutableLiveData<List<Person>> = MutableLiveData<List<Person>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
        .build()
        personService = build.create(PersonService::class.java)

    }

    fun getPersons(){
        personService.getAllPersons().enqueue(object : Callback<List<Person>>{
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>){
                if (response.isSuccessful){
                    val b: List<Person>? = response.body()
                    personsLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                }else{
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(person: Person, userId: String){
        personService.savePerson(person).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>){
                if (response.isSuccessful){
                    Log.d("APPLE", "Added: " + response.body())
                    getPersonsForUser(userId)
                }else{
                    val message = response.code().toString() + " " + response.message()
                    Log.d("APPLE", message)
                }
            }
            override fun onFailure(call: Call<Person>, t: Throwable){
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)

            }
        })

    }

    fun delete(id: Int, userId: String){
        personService.deletePerson(id).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>){
                if(response.isSuccessful){
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                    getPersonsForUser(userId)
                }else{
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(cal: Call<Person>, t: Throwable){
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(person: Person, userId: String){
        personService.updatePerson(person.id, person).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful){
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                    getPersonsForUser(userId)

                }else{
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable){
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun getPersonsForUser(userId: String) {
        personService.getPersonsByUserId(userId).enqueue(object : Callback<List<Person>> {

            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    // Update the LiveData with the new list of persons (friends)
                    personsLiveData.postValue(response.body())
                } else {
                    // Handle different types of errors
                    val message = when(response.code()) {
                        404 -> "No friends found for user."
                        500 -> "Server error. Please try again later."
                        else -> "Unexpected error: ${response.code()}"
                    }
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                // Network error or other unexpected errors
                errorMessageLiveData.postValue("Network error: ${t.message}")
            }
        })
    }

    fun sortByName(){
        personsLiveData.value = personsLiveData.value?.sortedBy { it.name.lowercase() }
    }

    fun sortByNameDescending(){
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.name.lowercase()}
    }

    fun sortByBirthday() {
        personsLiveData.value = personsLiveData.value?.sortedWith(compareBy(
            { it.birthDayOfMonth },
            { it.birthMonth },
            { it.birthYear }
        ))
    }

    fun sortByBirthdayDescending() {
        personsLiveData.value = personsLiveData.value?.sortedWith(
            compareByDescending<Person> { it.birthYear }
                .thenByDescending { it.birthMonth }
                .thenByDescending { it.birthDayOfMonth }
        )
    }

    fun sortByAge(){
        personsLiveData.value = personsLiveData.value?.sortedBy { person ->
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            currentYear - person.birthYear
        }
    }

    fun sortByAgeDescending(){
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.calculateAge() }
    }

    fun filterByName(name: String){
        if (name.isBlank()){
            getPersons()
        }else{
            personsLiveData.value = personsLiveData.value?.filter { person -> person.name.contains(name) }
        }
    }


}