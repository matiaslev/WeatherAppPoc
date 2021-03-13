package com.matiaslev.astropaypoc.di

import com.google.android.gms.location.LocationServices
import ar.com.uala.bancar.weatherapppoc.data.location.LocationManager
import ar.com.uala.bancar.weatherapppoc.data.location.LocationRepository
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherService
import ar.com.uala.bancar.weatherapppoc.ui.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // Geolocation
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single { LocationManager(get(), get()) }
    single { LocationRepository(get()) }

    // Weather Api
    single { provideRetrofit() }
    single { WeatherService(get()) }
    single { WeatherRepository(get()) }

    // Weather ViewModel
    viewModel { WeatherViewModel(get(), get()) }

}

fun provideRetrofit(
        // Potential dependencies of this type
): Retrofit {
    return Retrofit.Builder()
            .client(createHttpClient())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

private fun createHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    val httpClient = OkHttpClient.Builder()

    httpClient.interceptors().add(logging)

    return httpClient.build()
}