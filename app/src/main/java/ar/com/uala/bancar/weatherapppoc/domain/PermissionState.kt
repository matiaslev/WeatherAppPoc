package ar.com.uala.bancar.weatherapppoc.domain

sealed class PermissionState {
    object NotAsked : PermissionState()
    object NotGranted : PermissionState()
    object Granted : PermissionState()
}