package com.example.rnunningtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rnunningtracker.databinding.FragmentSetupBinding


class SetupFragment : Fragment() {

    private lateinit var binding: FragmentSetupBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupBinding.inflate(inflater)

        binding.tvContinue.setOnClickListener {
            findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToRunFragment())
        }

        return binding.root
    }


}