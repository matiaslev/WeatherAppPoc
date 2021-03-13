package ar.com.uala.bancar.weatherapppoc.data

import app.cash.turbine.test
import ar.com.uala.bancar.weatherapppoc.data.location.LocationManager
import ar.com.uala.bancar.weatherapppoc.data.location.LocationRepository
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime


@ExperimentalCoroutinesApi
@ExperimentalTime
class LocationRepositoryTest : BaseTest() {

    private lateinit var locationRepository: LocationRepository

    @MockK(relaxUnitFun = true)
    private lateinit var locationManager: LocationManager

    @Before
    fun init() {
        locationRepository = LocationRepository(locationManager)
    }

    @Test
    fun `if not have coarse location permission should offer the RequestPermissions state`() = runBlockingTest {

        // Arrange
        every { locationManager.hasCoarseLocationPermission() } returns true

        // Act
        val flow = locationRepository.getLastKnownLocation()

        // Assert
        flow.test {
            assertEquals(LocationState.RequestPermissions, expectItem())
        }

    }

    @Test
    fun `if have coarse location permission should not offer the RequestPermissions state`() = runBlockingTest {

        // Arrange
        every { locationManager.hasCoarseLocationPermission() } returns false

        // Act
        val flow = locationRepository.getLastKnownLocation()

        // Assert
        flow.test {
            expectNoEvents()
        }

    }

}