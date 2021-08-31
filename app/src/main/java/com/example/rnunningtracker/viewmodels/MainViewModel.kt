package com.example.rnunningtracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rnunningtracker.databse.Run
import com.example.rnunningtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    private val runsSortedByDate = mainRepository.getSortedRunByDate()
    private val runsSortedByAvgSpeed = mainRepository.getSortedRunByAvgSpeed()
    private val runsSortedByCalories = mainRepository.getSortedRunByCalories()
    private val runsSortedByTime = mainRepository.getSortedRunByTime()
    private val runsSortedByDistance = mainRepository.getSortedRunByDistance()

    val runs = MediatorLiveData<List<Run>>()
    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByAvgSpeed) { result ->
            if (sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByCalories) { result ->
            if (sortType == SortType.CALORIES) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByTime) { result ->
            if (sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runsSortedByDistance) { result ->
            if (sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) {
        when(sortType) {
            SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
            SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
            SortType.CALORIES -> runsSortedByCalories.value?.let { runs.value = it }
            SortType.RUNNING_TIME -> runsSortedByTime.value?.let { runs.value = it }
            SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        }.also {
            this.sortType = sortType
        }
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}


enum class SortType {
    DATE, AVG_SPEED, CALORIES, RUNNING_TIME, DISTANCE
}