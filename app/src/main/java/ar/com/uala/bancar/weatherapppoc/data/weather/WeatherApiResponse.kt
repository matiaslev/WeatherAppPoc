package ar.com.uala.bancar.weatherapppoc.data.weather

import com.google.gson.annotations.SerializedName

data class WeatherApiResponse (
        val coord: Coord,
        @SerializedName("weather") val weatherResponseList: List<WeatherResponse>,
        val base: String,
        val main: Main,
        val visibility: Long,
        val wind: Wind,
        val clouds: Clouds,
        val dt: Long,
        val sys: Sys,
        val timezone: Long,
        val id: Long,
        val name: String,
        val cod: Long
)

data class Clouds (
        val all: Long
)

data class Coord (
        val lon: Double,
        val lat: Double
)

data class Main (
        val temp: Double,
        @SerializedName("feels_like") val feelsLike: Double,
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max")val tempMax: Double,
        val pressure: Long,
        val humidity: Long
)

data class Sys (
        val type: Long,
        val id: Long,
        val country: String,
        val sunrise: Long,
        val sunset: Long
)

data class WeatherResponse (
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
)

data class Wind (
        val speed: Double,
        val deg: Long,
        val gust: Double
)
