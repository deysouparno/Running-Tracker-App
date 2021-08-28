package com.example.rnunningtracker.databse

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("select * from running_table order by timestamp desc")
    fun getAllRunsByDate(): LiveData<List<Run>>

    @Query("select * from running_table order by timeInMillis desc")
    fun getAllRunsByTime(): LiveData<List<Run>>

    @Query("select * from running_table order by caloriesBurnt desc")
    fun getAllRunsByCalories(): LiveData<List<Run>>

    @Query("select * from running_table order by avgSpeedInKmh desc")
    fun getAllRunsBySpeed(): LiveData<List<Run>>

    @Query("select * from running_table order by distanceInM desc")
    fun getAllRunsByDistance(): LiveData<List<Run>>

    @Query("select sum(timeInMillis) from running_table")
    fun getTotalRunTime(): LiveData<Long>

    @Query("select sum(caloriesBurnt) from running_table")
    fun getTotalCalories(): LiveData<Int>

    @Query("select sum(distanceInM) from running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("select avg(avgSpeedInKmh) from running_table")
    fun getTotalAvgSpeed(): LiveData<Float>

}