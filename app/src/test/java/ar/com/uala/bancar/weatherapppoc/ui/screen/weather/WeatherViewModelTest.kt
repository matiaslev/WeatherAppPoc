package ar.com.uala.bancar.weatherapppoc.ui.screen.weather

import app.cash.turbine.test
import ar.com.uala.bancar.weatherapppoc.data.location.LocationRepository
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.ui.WeatherViewModel
import ar.com.uala.bancar.weatherapppoc.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

    @Before
    fun init() {
        weatherViewModel = WeatherViewModel(locationRepository)
    }

    @Test
    fun `getLastKnownLocation should propagate the events from the locationRepository`() = runBlockingTest {

        // Arrange
        every { locationRepository.getLastKnownLocation() } returns flow {
            emit(LocationState.LastKnownLocation(weatherLocation))
        }

        // Act
        val flow = weatherViewModel.locationState
        weatherViewModel.getLastKnownLocation()

        // Assert
        flow.test {
            assertEquals(LocationState.LastKnownLocation(weatherLocation), expectItem())
        }

    }

}