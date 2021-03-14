package ar.com.uala.bancar.weatherapppoc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.uala.bancar.weatherapppoc.data.location.LocationRepository
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.domain.PermissionState
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class WeatherViewModel(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationState>(LocationState.NotRequested)
    val locationState: StateFlow<LocationState> = _locationState

    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.NotAsked)
    val permissionState: StateFlow<PermissionState> = _permissionState

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.NotInitialized)
    val weatherState: StateFlow<WeatherState> = _weatherState

    @ExperimentalCoroutinesApi
    fun getLastKnownLocation() {
        locationRepository.getLastKnownLocation()
            .onEach {
                when(it) {
                    is LocationState.LastKnownLocation -> {

                        weatherRepository.getWeatherByLocation(
                            latitude = it.weatherLocation.latitude.toString(),
                            longitude = it.weatherLocation.latitude.toString()
                        ).collect {
                            _weatherState.value = it
                        }

                    }

                    LocationState.NotKnownLocation,
                    LocationState.NotRequested,
                    LocationState.Processing,
                    LocationState.RequestPermissions -> {
                        _locationState.value = it
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    fun getWeatherByName(city: String) {
        weatherRepository.getWeatherByName(city)
                .onEach {
                    _weatherState.value = it
                }
                .launchIn(viewModelScope)
    }

    fun permissionGranted() {
        _permissionState.value = PermissionState.Granted
    }

    fun permissionNotGranted() {
        _permissionState.value = PermissionState.NotGranted
    }

    fun locationProcessing() {
        _locationState.value = LocationState.Processing
    }

}