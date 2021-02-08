package com.udacity.asteroidradar.main
import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainFragment.SelectedOption
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel (application: Application): ViewModel() {

    //define db variable
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidRepository(database)

    //all asteroids and POD available to application
    val asteroids = asteroidsRepository.asteroids
    val pictureOfDay = asteroidsRepository.pictureOfDay

    //handle asteroid filter
    private val _selectedOption = MutableLiveData<SelectedOption>()
    val selectedOption: LiveData<SelectedOption>
        get() = _selectedOption

    //navigation variable
    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

//    //list of asteroids
//   private val _asteroidList = MutableLiveData<List<DatabaseAsteroid>> ()
//    val databaseAsteroidList: LiveData<List<DatabaseAsteroid>>
//    get() = _asteroidList


    init {
        //refresh list from repo
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshPictureOfDay()
                showSelectedOption(SelectedOption.TODAY)
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    //load list of asteroids based on overflow menu filters
    val asteroidList = Transformations.switchMap(_selectedOption){
        when (it) {
            SelectedOption.TODAY -> asteroidsRepository.asteroids
            SelectedOption.SAVED -> asteroidsRepository.asteroidSaved
            SelectedOption.WEEK -> asteroidsRepository.asteroidsWeek
            else -> asteroidsRepository.asteroidSaved
        }
    }

    fun showSelectedOption(selectedOption: SelectedOption) {
        _selectedOption.value = selectedOption
    }

    //set navigation variable when asteroid item is clicked
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    //done navigating to details
    fun displayAsteroidsDone() {
        _navigateToDetails.value = null
    }

//    private fun populateList() {
//
//        val a1 = DatabaseAsteroid(0L, "codeName", "10/10/2021", 2.4, 1.2,
//        456.3, 12.3, true)
//
//
//        val a2 = DatabaseAsteroid(1L, "code", "13/10/2021", 34.4, 2.2,
//            4.3, 120.3, false)
//
//        val a3 = DatabaseAsteroid(3L, "codeName", "10/04/2021", 2.4, 1.2,
//            4.3, 2.3, true)
//
//            var list = mutableListOf<DatabaseAsteroid>(a1, a2, a3)
//
//        _asteroidList.value = list
//    }

    /*
    * Factory for constructing MainViewModel with param
    */
    @Suppress("UNCHECKED_CAST")
    class Factory(val app: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}