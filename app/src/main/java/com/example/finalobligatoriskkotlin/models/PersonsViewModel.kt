package com.example.finalobligatoriskkotlin.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.finalobligatoriskkotlin.repository.PersonRepository
import com.google.firebase.auth.FirebaseAuth
import java.sql.RowId

class PersonsViewModel : ViewModel() {
    private val repository = PersonRepository()
    val personsLiveData: LiveData<List<Person>> = repository.personsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        if (userUID != null) {
            repository.getPersonsForUser(userUID)
        } else {
            // If no user is logged in, fetch all persons as a fallback
            repository.getPersons()
        }
    }

    fun reload(){
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        if (userUID != null) {
            repository.getPersonsForUser(userUID)
        } else {
            // If no user is logged in, fetch all persons as a fallback
            repository.getPersons()
        }
    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }

    fun add(person: Person, userId: String){
        repository.add(person, userId)
    }

    fun delete(id: Int, userId: String){
        repository.delete(id, userId)
    }
    fun update(person: Person, userId: String){
        repository.update(person, userId)
    }
    fun sortByName(){
        repository.sortByName()
    }

    fun sortByNameDescending(){
        repository.sortByNameDescending()
    }

    fun sortByAge(){
        repository.sortByAge()
    }

    fun sortByAgeDescending(){
        repository.sortByAgeDescending()
    }

    fun sortByBirthday(){
        repository.sortByBirthday()
    }

    fun sortByBirthdayDescending(){
        repository.sortByBirthdayDescending()
    }


    fun filterByAge(minAge: Int, maxAge: Int) {
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        if (userUID != null) {
            repository.filterByAge(minAge, maxAge, userUID)
        }
    }

    fun filterByName(name: String) {
        val userUID = FirebaseAuth.getInstance().currentUser!!.uid // As we know the user is logged in
        repository.filterByName(name, userUID)
    }


    fun getPersonById(id: Int): Person? {
        return personsLiveData.value?.firstOrNull { it.id == id }
    }
    fun getPersonsForUser(userId: String) {
        repository.getPersonsForUser(userId)
    }



}