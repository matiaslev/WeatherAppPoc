package ar.com.uala.bancar.weatherapppoc.domain

sealed class WeatherState {
    object NotInitialized: WeatherState()
    object Loading: WeatherState()
    data class Success(val weather: Weather): WeatherState()
    data class Error(val exception: Throwable): WeatherState()
}