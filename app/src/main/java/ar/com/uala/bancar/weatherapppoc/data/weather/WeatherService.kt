package ar.com.uala.bancar.weatherapppoc.data.weather

import retrofit2.Retrofit

class WeatherService(
        retrofit: Retrofit
) {

    companion object {
        const val STATIC_NOT_SECURE = "390535ae73e39302dcff3fbdebf4502b"
    }

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    suspend fun searchByName(name: String) = weatherApi.getByName(
            name = name,
            token = STATIC_NOT_SECURE
    )

    suspend fun searchByLocation(latitude: String, longitude: String) = weatherApi.getByLocation(
        latitude = latitude,
        longitude = longitude,
        token = STATIC_NOT_SECURE
    )

}