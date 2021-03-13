package ar.com.uala.bancar.weatherapppoc.data.location

import android.annotation.SuppressLint
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import com.matiaslev.astropaypoc.domain.WeatherLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class LocationRepository(
    private val locationManager: LocationManager
) {

    @SuppressLint("MissingPermission") // Contemplated inside hasCoarseLocationPermission
    fun getLastKnownLocation() = callbackFlow {
        if (locationManager.hasCoarseLocationPermission()) {
            offer(LocationState.RequestPermissions)
        }

        locationManager.getLastKnownLocation {
            if (it != null) {
                val weatherLocation = WeatherLocation(it.latitude, it.longitude)
                offer(LocationState.LastKnownLocation(weatherLocation))
            } else {
                offer(LocationState.NotKnownLocation)
            }
        }

        awaitClose { cancel() }
    }

}