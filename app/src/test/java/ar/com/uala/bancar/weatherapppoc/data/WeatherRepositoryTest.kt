package ar.com.uala.bancar.weatherapppoc.data

import app.cash.turbine.test
import ar.com.uala.bancar.weatherapppoc.BaseTest
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository.Companion.YourCurrentLocation
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherService
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class WeatherRepositoryTest : BaseTest() {

    private lateinit var weatherRepository: WeatherRepository

    @MockK
    private lateinit var weatherService: WeatherService

    @Before
    fun init() {
        weatherRepository = WeatherRepository(weatherService)
    }

    @Test
    fun `getWeatherByLocation should call searchByLocation and propagate WeatherState`() = runBlockingTest {
        // Arrange
        coEvery {
            weatherService.searchByLocation(
                weatherLocationMock.latitude.toString(),
                weatherLocationMock.longitude.toString()
            )
        } returns weatherApiResponseMock

        // Act
        val flow = weatherRepository.getWeatherByLocation(
            weatherLocationMock.latitude.toString(),
            weatherLocationMock.longitude.toString()
        )

        // Assert
        flow.test {
            assertEquals(WeatherState.Loading, expectItem())
            assertEquals(WeatherState.Success(weatherMock), expectItem())
            expectComplete()
        }

    }

    @Test
    fun `getWeatherByLocation shoul return "Your Current Location" as name when is empty`() = runBlockingTest {
        // Arrange
        coEvery {
            weatherService.searchByLocation(
                    weatherLocationMock.latitude.toString(),
                    weatherLocationMock.longitude.toString()
            )
        } returns weatherApiResponseMock.copy(
                name = EmptyString
        )

        // Act
        val flow = weatherRepository.getWeatherByLocation(
                weatherLocationMock.latitude.toString(),
                weatherLocationMock.longitude.toString()
        )

        // Assert
        flow.test {
            assertEquals(WeatherState.Loading, expectItem())
            assert((expectItem() as WeatherState.Success).weather.city == YourCurrentLocation)
            expectComplete()
        }

    }
}