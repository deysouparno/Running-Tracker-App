package com.example.rnunningtracker.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rnunningtracker.R
import com.example.rnunningtracker.databinding.FragmentSettingsBinding
import com.example.rnunningtracker.utils.Constants.KEY_NAME
import com.example.rnunningtracker.utils.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        loadData()

        binding.btnApplyChanges.setOnClickListener {
            if (applyChangesToSharedPref()) {
                Snackbar.make(binding.root, "Data Updated Successfully", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Please Fill all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun loadData() {
        binding.apply {
            etName.setText(sharedPref.getString(KEY_NAME, "")?: "")
            etWeight.setText(sharedPref.getFloat(KEY_WEIGHT, 60f).toString())
        }
    }

    private fun applyChangesToSharedPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty()) return false

        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .apply()

        requireActivity().findViewById<MaterialTextView>(R.id.tvToolbarTitle).text = "Let's go $name"
        return true
    }


}