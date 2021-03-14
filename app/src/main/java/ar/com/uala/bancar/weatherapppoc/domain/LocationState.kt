package ar.com.uala.bancar.weatherapppoc.domain

sealed class LocationState {
    object NotRequested : LocationState()
    object NotKnownLocation : LocationState()
    data class LastKnownLocation(val weatherLocation: WeatherLocation) : LocationState()
    object RequestPermissions : LocationState()
}