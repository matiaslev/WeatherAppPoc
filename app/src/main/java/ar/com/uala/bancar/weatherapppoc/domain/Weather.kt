package ar.com.uala.bancar.weatherapppoc.domain

data class Weather(
        val city: String,
        val temp: Double,
        val tempMin: Double,
        val tempMax: Double,
        val tempFeeling: Double,
        val image: String,
        val windSpeed: String
)