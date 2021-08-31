package com.example.rnunningtracker.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rnunningtracker.adapters.RunAdapter
import com.example.rnunningtracker.databinding.FragmentRunBinding
import com.example.rnunningtracker.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.rnunningtracker.utils.TrackingUtility
import com.example.rnunningtracker.viewmodels.MainViewModel
import com.example.rnunningtracker.viewmodels.SortType
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class RunFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            EasyPermissions.onRequestPermissionsResult()
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value


            }
        }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var runAdapter: RunAdapter
    private lateinit var binding: FragmentRunBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunBinding.inflate(inflater)

        binding.fab.setOnClickListener {
            findNavController().navigate(RunFragmentDirections.actionRunFragmentToTrackingFragment())
        }

//        permissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION))
        requestPermission()
        setUpRecyclerView()

        when (viewModel.sortType) {
            SortType.DATE -> binding.spFilter.setSelection(0)
            SortType.RUNNING_TIME -> binding.spFilter.setSelection(1)
            SortType.DISTANCE -> binding.spFilter.setSelection(2)
            SortType.AVG_SPEED -> binding.spFilter.setSelection(3)
            SortType.CALORIES -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALORIES)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, {
            runAdapter.submitList(it)
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        runAdapter = RunAdapter()
        binding.rvRuns.apply {
            adapter = runAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "this app needs this permission to work properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "this app needs this permission to work properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) = Unit

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}