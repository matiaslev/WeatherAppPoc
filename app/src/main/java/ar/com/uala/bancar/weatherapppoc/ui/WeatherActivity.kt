package ar.com.uala.bancar.weatherapppoc.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ar.com.uala.bancar.weatherapppoc.R
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.domain.PermissionState
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import coil.load
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class WeatherActivity : AppCompatActivity() {

    companion object {
        const val DefaultTitle = "Find Your Weather"
        const val OkDialogButtonText = "OK"
        const val CancelDialogButtonText = "Cancel"

        // Cities
        const val Montevideo = "Montevideo"
        const val London = "London"
        const val SanPablo = "San Pablo"
        const val BuenosAires = "Buenos Aires"
        const val Munich = "Munich"
    }

    val weatherViewModel: WeatherViewModel by viewModel()
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_layout)

        title = DefaultTitle
        stateChanges()

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                weatherViewModel.getLastKnownLocation()
            } else {
                weatherViewModel.permissionNotGranted()
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            getLastKnownLocationOrAskForCoarseLocationPermission()
        }
    }

    private fun stateChanges() {
        weatherViewModel.locationState
            .onEach {
                findViewById<TextView>(R.id.temp).apply {
                    text = when(it) {
                        LocationState.NotRequested -> getString(R.string.request_your_weather)

                        LocationState.RequestPermissions -> getString(R.string.request_location_permissions)

                        LocationState.NotKnownLocation -> getString(R.string.cant_get_location)

                        else -> ""
                    }
                }
            }
            .launchIn(lifecycleScope)

        weatherViewModel.permissionState
            .onEach {
                when(it) {
                    PermissionState.NotGranted -> {
                        findViewById<TextView>(R.id.temp).text = getString(R.string.request_location_permissions)
                    }

                    PermissionState.Granted -> {

                    }

                    PermissionState.NotAsked -> {

                    }
                }
            }
            .launchIn(lifecycleScope)

        weatherViewModel.weatherState
            .onEach {
                when(it) {
                    is WeatherState.Success -> {
                        title = getString(R.string.city_title, it.weather.city)

                        findViewById<ImageView>(R.id.weather_image).load(it.weather.image)

                        findViewById<TextView>(R.id.temp).text = resources
                            .getString(R.string.current_temp, it.weather.temp.toString())

                        findViewById<TextView>(R.id.temp_min).text = resources
                            .getString(R.string.min_temp, it.weather.tempMin.toString())

                        findViewById<TextView>(R.id.temp_max).text = resources
                            .getString(R.string.max_temp, it.weather.tempMax.toString())

                        findViewById<TextView>(R.id.wind).text = resources
                            .getString(R.string.wind_speed, it.weather.windSpeed)
                    }
                    is WeatherState.Error -> {
                        Toast.makeText(this, "Error: ${it.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    WeatherState.Loading -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    WeatherState.NotInitialized -> {

                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun getLastKnownLocationOrAskForCoarseLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {

                weatherViewModel.permissionGranted()
                weatherViewModel.getLastKnownLocation()

            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {

                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(this)
                    builder.apply {
                        setPositiveButton(OkDialogButtonText) { dialog, id ->
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                        }
                        setNegativeButton(CancelDialogButtonText) { dialog, id ->
                            weatherViewModel.permissionNotGranted()
                        }
                    }
                    // Set other dialog properties


                    // Create the AlertDialog
                    builder.create()
                }

                alertDialog?.show()

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_montevideo ->
                    if (checked) {
                        weatherViewModel.getWeatherByName(Montevideo)
                    }
                R.id.radio_londres ->
                    if (checked) {
                        weatherViewModel.getWeatherByName(London)
                    }
                R.id.radio_san_pablo ->
                    if (checked) {
                        weatherViewModel.getWeatherByName(SanPablo)
                    }
                R.id.radio_buenos_aires ->
                    if (checked) {
                        weatherViewModel.getWeatherByName(BuenosAires)
                    }
                R.id.radio_munich ->
                    if (checked) {
                        weatherViewModel.getWeatherByName(Munich)
                    }
            }
        }
    }

}