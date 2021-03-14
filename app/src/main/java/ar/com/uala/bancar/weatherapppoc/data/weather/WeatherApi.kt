package ar.com.uala.bancar.weatherapppoc.data.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val UNITS = "metric"
    }

    @GET("weather/")
    suspend fun getByName(
            @Query("q") name: String,
            @Query("APPID") token: String,
            @Query("units") units: String = UNITS
    ): WeatherApiResponse

    @GET("weather/")
    suspend fun getByLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("APPID") token: String,
        @Query("units") units: String = UNITS
    ): WeatherApiResponse

}