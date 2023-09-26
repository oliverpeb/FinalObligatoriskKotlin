package com.example.finalobligatoriskkotlin.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.finalobligatoriskkotlin.repository.PersonRepository

class PersonsViewModel : ViewModel() {
    private val repository = PersonRepository()
    val personsLiveData: LiveData<List<Person>> = repository.personsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init{
        reload()
    }

    fun reload(){
        repository.getPersons()
    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }

    fun add(person: Person){
        repository.add(person)
    }

    fun delete(id: Int){
        repository.delete(id)
    }
    fun update(person: Person){
        repository.update(person)
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

    fun filterByName(name: String){
        repository.filterByName(name)
    }

}