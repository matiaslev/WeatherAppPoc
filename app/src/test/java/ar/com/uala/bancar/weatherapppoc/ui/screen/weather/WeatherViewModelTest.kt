package ar.com.uala.bancar.weatherapppoc.ui.screen.weather

import app.cash.turbine.test
import ar.com.uala.bancar.weatherapppoc.BaseTest
import ar.com.uala.bancar.weatherapppoc.data.location.LocationRepository
import ar.com.uala.bancar.weatherapppoc.data.weather.WeatherRepository
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import ar.com.uala.bancar.weatherapppoc.ui.WeatherViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class WeatherViewModelTest : BaseTest() {

    private lateinit var weatherViewModel: WeatherViewModel

    @MockK
    private lateinit var locationRepository: LocationRepository

    @MockK
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun init() {
        weatherViewModel = WeatherViewModel(locationRepository, weatherRepository)
    }

    @Test
    fun `LastKnownLocation should call getWeatherByLocation and propagate weatherState`() = runBlockingTest {

        // Arrange
        every { locationRepository.getLastKnownLocation() } returns flow {
            emit(LocationState.LastKnownLocation(weatherLocationMock))
        }

        every { weatherRepository.getWeatherByLocation(
            weatherLocationMock.latitude.toString(),
            weatherLocationMock.longitude.toString()
        )
        } returns flow {
            emit(WeatherState.Success(weatherMock))
        }

        // Act
        val flow = weatherViewModel.weatherState
        weatherViewModel.getLastKnownLocation()

        // Assert
        verify(exactly = 1) {
            weatherRepository.getWeatherByLocation(
                weatherLocationMock.latitude.toString(),
                weatherLocationMock.longitude.toString()
            )
        }

        flow.test {
            assertEquals(WeatherState.Success(weatherMock), expectItem())
        }

    }

    @Test
    fun `NotKnownLocation should propagate locationState`() = runBlockingTest {

        // Arrange
        every { locationRepository.getLastKnownLocation() } returns flow {
            emit(LocationState.NotKnownLocation)
        }

        // Act
        val flow = weatherViewModel.locationState
        weatherViewModel.getLastKnownLocation()

        // Assert
        flow.test {
            assertEquals(LocationState.NotKnownLocation, expectItem())
        }

    }

    @Test
    fun `NotRequested should propagate locationState`() = runBlockingTest {

        // Arrange
        every { locationRepository.getLastKnownLocation() } returns flow {
            emit(LocationState.NotRequested)
        }

        // Act
        val flow = weatherViewModel.locationState
        weatherViewModel.getLastKnownLocation()

        // Assert
        flow.test {
            assertEquals(LocationState.NotRequested, expectItem())
        }

    }

    @Test
    fun `RequestPermissions should propagate locationState`() = runBlockingTest {

        // Arrange
        every { locationRepository.getLastKnownLocation() } returns flow {
            emit(LocationState.RequestPermissions)
        }

        // Act
        val flow = weatherViewModel.locationState
        weatherViewModel.getLastKnownLocation()

        // Assert
        flow.test {
            assertEquals(LocationState.RequestPermissions, expectItem())
        }

    }

}