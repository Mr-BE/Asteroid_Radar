package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    //list of asteroids
   private val _asteroidList = MutableLiveData<List<Asteroid>> ()
    val asteroidList: LiveData<List<Asteroid>>
    get() = _asteroidList


    init {
        populateList()
    }

    private fun populateList() {

        val a1 = Asteroid(0L, "codeName", "10/10/2021", 2.4, 1.2,
        456.3, 12.3, true)


        val a2 = Asteroid(1L, "code", "13/10/2021", 34.4, 2.2,
            4.3, 120.3, false)

        val a3 = Asteroid(3L, "codeName", "10/04/2021", 2.4, 1.2,
            4.3, 2.3, true)

            var list = mutableListOf<Asteroid>(a1, a2, a3)

        _asteroidList.value = list
    }

}