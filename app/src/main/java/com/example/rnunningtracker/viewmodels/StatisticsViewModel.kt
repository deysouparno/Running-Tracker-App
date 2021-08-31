package com.example.rnunningtracker.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rnunningtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor (
    val mainRepository: MainRepository
): ViewModel() {
    val totalTimeRun = mainRepository.getTotalRunTime()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
    val totalCalories = mainRepository.getTotalCalories()
    val totalDistance = mainRepository.getTotalDistance()

    val runsSortedByDate = mainRepository.getSortedRunByDate()

}