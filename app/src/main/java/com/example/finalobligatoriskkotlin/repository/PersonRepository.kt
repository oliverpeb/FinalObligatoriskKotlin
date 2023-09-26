package com.example.finalobligatoriskkotlin.repository

import androidx.lifecycle.MutableLiveData
import com.example.finalobligatoriskkotlin.models.Person
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log



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
        getPersons()
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

    fun add(person: Person){
        personService.savePerson(person).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>){
                if (response.isSuccessful){
                    Log.d("APPLE", "Added: " + response.body())
                    getPersons()
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

    fun delete(id: Int){
        personService.deletePerson(id).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>){
                if(response.isSuccessful){
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
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

    fun update(person: Person){
        personService.updatePerson(person.id, person).enqueue(object : Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if(response.isSuccessful){
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())

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

    fun sortByName(){
        personsLiveData.value = personsLiveData.value?.sortedBy { it.name }
    }

    fun sortByNameDescending(){
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.name }
    }

    fun sortByAge(){
        personsLiveData.value = personsLiveData.value?.sortedBy { it.age }
    }

    fun sortByAgeDescending(){
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.age }
    }

    fun filterByName(name: String){
        if (name.isBlank()){
            getPersons()
        }else{
            personsLiveData.value = personsLiveData.value?.filter { person -> person.name.contains(name) }
        }
    }
}