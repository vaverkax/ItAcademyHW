package com.example.itacademyhw

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("MissingPermission")
class LocationService(private val context: Context) {

    private var myLocation: Location? = null
    private var locationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationFlow: Flow<Location> = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                myLocation = locationResult.lastLocation
                trySend(locationResult.lastLocation)
            }
        }

        val request = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            locationClient.removeLocationUpdates(callback)
            myLocation = null
        }
    }

    private suspend fun lastKnownLocation(): Location? = suspendCoroutine { cont ->
        locationClient.lastLocation
            .addOnSuccessListener {
                cont.resume(it)
            }
            .addOnCanceledListener {
                cont.resume(null)
            }
            .addOnFailureListener{
                cont.resume(null)
            }
    }

    suspend fun getLocation(): Location? {
        return  myLocation ?: lastKnownLocation()
    }

    companion object {
        private const val LOCATION_UPDATE_INTERVAL = 2000L
    }
}