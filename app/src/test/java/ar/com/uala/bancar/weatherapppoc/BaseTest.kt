package ar.com.uala.bancar.weatherapppoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.com.uala.bancar.weatherapppoc.data.weather.*
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository.Companion.WeatherIconAfterIcon
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository.Companion.WeatherIconUrlBeforeIcon
import ar.com.uala.bancar.weatherapppoc.domain.Weather
import ar.com.uala.bancar.weatherapppoc.domain.WeatherLocation
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseTest {

    companion object {
        const val EmptyString = ""

        val weatherLocationMock = WeatherLocation(0.00, 0.00)

        val weatherMock = Weather(
            city = "Mar Del Plata",
            temp = 38.00,
            tempMin = 10.00,
            tempMax = 40.00,
            tempFeeling = 32.00,
            image = WeatherIconUrlBeforeIcon +"image.png" + WeatherIconAfterIcon,
            windSpeed = "100.0"
        )
        val weatherApiResponseMock = WeatherApiResponse(
            coord = Coord(0.00, 0.00),
            weatherResponseList = listOf(WeatherResponse(1, EmptyString, EmptyString, "image.png")),
            base = EmptyString,
            main = Main(weatherMock.temp, weatherMock.tempFeeling, weatherMock.tempMin, weatherMock.tempMax, 0, 0),
            visibility = 0,
            wind = Wind(weatherMock.windSpeed.toDouble(), 0, 0.00),
            clouds = Clouds(3),
            dt = 0,
            sys = Sys(0,0,"", 0, 0),
            timezone = 0,
            id = 0,
            name = weatherMock.city,
            cod = 0
        )
    }

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}