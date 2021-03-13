package ar.com.uala.bancar.weatherapppoc.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class LocationManager(
    private val focusedLocationProviderClient: FusedLocationProviderClient,
    private val context: Context
) {

    fun hasCoarseLocationPermission(): Boolean = ActivityCompat
        .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission") // Contemplated in hasCoarseLocationPermission
    fun getLastKnownLocation(action: (Location?) -> Unit) {
        focusedLocationProviderClient.lastLocation.addOnSuccessListener {
            action(it)
        }
    }

}