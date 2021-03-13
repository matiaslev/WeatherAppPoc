package ar.com.uala.bancar.weatherapppoc.data.weather

import retrofit2.Retrofit

class WeatherService(
        private val retrofit: Retrofit
) {

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    suspend fun searchByName(name: String) = weatherApi.getByName(
            name = name,
            token = "390535ae73e39302dcff3fbdebf4502b"
    )

    suspend fun searchByLocation(latitude: String, longitude: String) = weatherApi.getByLocation(
        latitude = latitude,
        longitude = longitude,
        token = "390535ae73e39302dcff3fbdebf4502b"
    )

}