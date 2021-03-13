package ar.com.uala.bancar.weatherapppoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.com.uala.bancar.weatherapppoc.data.weather.*
import ar.com.uala.bancar.weatherapppoc.domain.Weather
import com.matiaslev.astropaypoc.domain.WeatherLocation
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
        const val EMPTY_STRING = ""

        val weatherLocationMock = WeatherLocation(0.00, 0.00)

        val weatherMock = Weather(
            temp = 38.00
        )
        val weatherApiResponseMock = WeatherApiResponse(
            coord = Coord(0.00, 0.00),
            weatherResponseList = listOf(),
            base = EMPTY_STRING,
            main = Main(weatherMock.temp, 0.00, 0.00, 0.00, 0, 0),
            visibility = 0,
            wind = Wind(0.00, 0, 0.00),
            clouds = Clouds(3),
            dt = 0,
            sys = Sys(0,0,"", 0, 0),
            timezone = 0,
            id = 0,
            name = EMPTY_STRING,
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