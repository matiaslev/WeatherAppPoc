package ar.com.uala.bancar.weatherapppoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
        val weatherLocation = WeatherLocation(0.00, 0.00)
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