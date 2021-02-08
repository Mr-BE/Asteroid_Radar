package com.udacity.asteroidradar.work

import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    //coroutines scope
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        preInit()

    }

    //launch coroutine
    private fun preInit() = applicationScope.launch {
        setUpRecurringWork()
    }

    //re-occurring work
    private fun setUpRecurringWork() {
        //constraints for running background work
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        //set up repeating requests
        val repeatingRequest = PeriodicWorkRequestBuilder<DeleteDataWork>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()

        //schedule work as unique
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DeleteDataWork.WORK_NAME,
            //keep existing request if one already exist
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }


}