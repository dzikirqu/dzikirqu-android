package com.dzikirqu.android.util


import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


object SingleLocationProvider {

    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    @SuppressLint("MissingPermission", "CheckResult")
    fun requestSingleUpdate(context: Context, onSuccess:(GPSCoordinates)->Unit){
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000
        var locationCallback: LocationCallback? = null

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        onSuccess(GPSCoordinates(location.latitude,location.longitude))
                        mFusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                }
            }
        }
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    // consider returning Location instead of this dummy wrapper class
    class GPSCoordinates(theLatitude: Double, theLongitude: Double) {
        var longitude = -1f
        var latitude = -1f

        init {
            longitude = theLongitude.toFloat()
            latitude = theLatitude.toFloat()
        }
    }
}
