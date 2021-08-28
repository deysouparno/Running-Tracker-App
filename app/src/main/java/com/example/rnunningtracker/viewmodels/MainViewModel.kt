package com.example.rnunningtracker.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rnunningtracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    val mainRepository: MainRepository
): ViewModel() {
}