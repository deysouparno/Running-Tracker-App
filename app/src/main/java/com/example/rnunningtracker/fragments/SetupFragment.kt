package com.example.rnunningtracker.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.rnunningtracker.R
import com.example.rnunningtracker.databinding.FragmentSetupBinding
import com.example.rnunningtracker.utils.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.rnunningtracker.utils.Constants.KEY_NAME
import com.example.rnunningtracker.utils.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : Fragment() {

    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupBinding.inflate(inflater)

        if (isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true).build()

            findNavController().navigate(R.id.action_setupFragment_to_runFragment, savedInstanceState, navOptions)
        }

        binding.tvContinue.setOnClickListener {
            val success = writeDataToSharedPref()

            if (success) {
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToRunFragment())
            } else {
                Snackbar.make(requireView(), "Please enter valid details", Snackbar.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun writeDataToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty()) return false
        sharedPref.edit().putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        requireActivity().findViewById<MaterialTextView>(R.id.tvToolbarTitle).text = "Let's go $name"
        return true
    }


}