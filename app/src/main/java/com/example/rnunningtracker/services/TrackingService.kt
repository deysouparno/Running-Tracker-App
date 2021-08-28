package com.example.rnunningtracker.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.rnunningtracker.MainActivity
import com.example.rnunningtracker.R
import com.example.rnunningtracker.utils.Constants.ACTION_PAUSE_SERVICE
import com.example.rnunningtracker.utils.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.rnunningtracker.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.rnunningtracker.utils.Constants.ACTION_STOP_SERVICE
import com.example.rnunningtracker.utils.Constants.FASTEST_LOCATION_INTERVAL
import com.example.rnunningtracker.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.example.rnunningtracker.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.rnunningtracker.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.rnunningtracker.utils.Constants.NOTIFICATION_ID
import com.example.rnunningtracker.utils.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {

    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Timber.d("New location: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, {
            updateLocationTracking(it)
        })
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addEmptyPolyline() {
        pathPoints.value?.apply {
            add(mutableListOf())
            pathPoints.postValue(this)
        } ?: pathPoints.postValue(mutableListOf(mutableListOf()))
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("Started or resume service")
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startForegroundService()
                    }

                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> Timber.d("Stopped service")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        isTracking.postValue(true)
        addEmptyPolyline()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificaionBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running Tracker")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificaionBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

}