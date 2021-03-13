package ar.com.uala.bancar.weatherapppoc.data.weather

import ar.com.uala.bancar.weatherapppoc.domain.Weather
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRepository(
        private val weatherService: WeatherService
) {

    companion object {
        const val YourCurrentLocation = "Your Current Location"
    }

    fun getWeatherByName(city: String) = flow {

        emit(WeatherState.Loading)

        val weather = weatherService.searchByName(city).run {
            Weather(
                city = name,
                temp = main.temp
            )
        }
        emit(WeatherState.Success(weather))

    }.catch { exception ->

        emit(WeatherState.Error(exception))

    }

    fun getWeatherByLocation(latitude: String, longitude: String) = flow {

        emit(WeatherState.Loading)

        val weather = weatherService.searchByLocation(latitude, longitude).run {
            Weather(
                city = if (name.isEmpty()) YourCurrentLocation else name,
                temp = main.temp
            )
        }
        emit(WeatherState.Success(weather))

    }.catch { exception ->

        emit(WeatherState.Error(exception))

    }

}