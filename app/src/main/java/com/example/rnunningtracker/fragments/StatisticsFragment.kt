package com.example.rnunningtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rnunningtracker.R
import com.example.rnunningtracker.databinding.FragmentStatisticsBinding
import com.example.rnunningtracker.utils.TrackingUtility
import com.example.rnunningtracker.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val viewmodel: StatisticsViewModel by viewModels()
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticsBinding.inflate(inflater)
        subscribeToObservers()
        return binding.root
    }

    private fun subscribeToObservers() {

        viewmodel.apply {
            totalTimeRun.observe(viewLifecycleOwner, {
                it?.let {
                    binding.tvTotalTime.text = TrackingUtility.getFormattedStopWatchTime(it)
                }
            })

            totalDistance.observe(viewLifecycleOwner, {
                it?.let {
                    binding.tvTotalDistance.text = "${round((it / 1000f) * 10f) / 10f} km"
                }
            })

            totalAvgSpeed.observe(viewLifecycleOwner, {
                it?.let {
                    binding.tvAverageSpeed.text = "${round(it * 10f) / 10f} km/h"
                }
            })

            totalCalories.observe(viewLifecycleOwner, {
                it?.let {
                    binding.tvTotalCalories.text = "$it kcal"

                }
            })
        }



    }

}