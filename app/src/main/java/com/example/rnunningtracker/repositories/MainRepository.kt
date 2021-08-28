package com.example.rnunningtracker.repositories

import com.example.rnunningtracker.databse.Run
import com.example.rnunningtracker.databse.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun insertRun(run: Run) {
        runDao.insertRun(run)
    }

    suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run)
    }

    fun getSortedRunByDate() = runDao.getAllRunsByDate()

    fun getSortedRunByDistance() = runDao.getAllRunsByDistance()

    fun getSortedRunByCalories() = runDao.getAllRunsByCalories()

    fun getSortedRunByTime() = runDao.getAllRunsByTime()

    fun getSortedRunByAvgSpeed() = runDao.getAllRunsBySpeed()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalCalories() = runDao.getTotalCalories()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()

    fun getTotalRunTime() = runDao.getTotalRunTime()
}