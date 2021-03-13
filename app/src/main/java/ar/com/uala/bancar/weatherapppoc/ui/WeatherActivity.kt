package ar.com.uala.bancar.weatherapppoc.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ar.com.uala.bancar.weatherapppoc.R
import ar.com.uala.bancar.weatherapppoc.domain.LocationState
import ar.com.uala.bancar.weatherapppoc.domain.PermissionState
import ar.com.uala.bancar.weatherapppoc.domain.WeatherState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class WeatherActivity : AppCompatActivity() {

    val weatherViewModel: WeatherViewModel by viewModel()
    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_layout)

        weatherViewModel.locationState
                .onEach {
                    findViewById<TextView>(R.id.text).apply {
                        text = when(it) {
                            is LocationState.LastKnownLocation -> {
                                "Lat: ${it.weatherLocation?.latitude} \n Long: ${it.weatherLocation?.longitude}"
                            }
                            LocationState.NotRequested -> {
                                "Not Requested"
                            }
                            LocationState.RequestPermissions -> "Permissions Needed"
                            LocationState.NotKnownLocation -> "We don't know"
                        }
                    }
                }
                .launchIn(lifecycleScope)

        weatherViewModel.permissionState
                .onEach {
                    when(it) {
                        PermissionState.NotGranted -> {
                            findViewById<TextView>(R.id.text).text = "We need the permission dude"
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
                        Toast.makeText(this, "Temp: ${it.weather.temp}", Toast.LENGTH_SHORT).show()
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
                        setPositiveButton("ok") { dialog, id ->
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                        }
                        setNegativeButton("cancel") { dialog, id ->
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

}