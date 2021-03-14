package ar.com.uala.bancar.weatherapppoc.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationManager(
    private val focusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context
) {

    private lateinit var locationCallback: LocationCallback

    fun notHasCoarseLocationPermission(): Boolean = ActivityCompat
        .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission") // Contemplated in hasCoarseLocationPermission
    fun getLastKnownLocation(action: (Location?) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                action(locationResult.lastLocation)
                focusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

        focusedLocationProviderClient.lastLocation.addOnSuccessListener {
            action(it)
            /** TODO: In some cases the Location Could be Null, we could listen location updates
             * Documentation: https://developer.android.com/training/location/retrieve-current
             * */
            // startLocationUpdates(locationCallback)
        }
    }

    @SuppressLint("MissingPermission") // Contemplated in getLastKnownLocation process
    fun startLocationUpdates(locationCallback: LocationCallback) {
        focusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

}