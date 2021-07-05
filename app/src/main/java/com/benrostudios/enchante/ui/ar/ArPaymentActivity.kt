package com.benrostudios.enchante.ui.ar

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.benrostudios.enchante.R
import com.benrostudios.enchante.data.models.ar.GeometryLocation
import com.benrostudios.enchante.data.models.ar.Place
import com.benrostudios.enchante.data.models.ar.getPositionVector
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.nearby.messages.*
import com.google.android.gms.tasks.Task
import com.google.ar.core.Anchor
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode


class ArPaymentActivity : AppCompatActivity(), SensorEventListener {

    //AR
    private var anchorNode: AnchorNode? = null
    private lateinit var arFragment: ArPaymentFragment

    // Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    // Sensor
    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var places: List<Place>? =
        listOf(Place("test point", "test_icon", "test_name", GeometryLocation(9.123123, 54.12312)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_payment)
        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArPaymentFragment
        sensorManager = (getSystemService() as SensorManager?)!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d(TAG, "LAT: ${location.latitude} LONG: ${location.longitude}")
                    val latLng = LatLng(location.latitude, location.longitude)
                    currentLocation = location
                }
            }
        }
        initialiseLocationWithPerms()
        setUpAr()
    }

    private fun setUpAr() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            // Create anchor
            val anchor = hitResult.createAnchor()
            anchorNode = AnchorNode(anchor)
            anchorNode?.setParent(arFragment.arSceneView.scene)
            addPlaces(anchorNode!!)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

//    private fun getCurrentLocation(onSuccess: (Location) -> Unit) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            Log.d(TAG, "Location found!!! ${location.longitude}, ${location.latitude}")
//            currentLocation = location
//            onSuccess(location)
//        }.addOnFailureListener {
//            Log.e(TAG, "Could not get location")
//        }
//    }

    private fun initialiseLocationWithPerms() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@addOnSuccessListener
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this,
                        12
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }

        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "ar_payment_activity"
    }

    private fun addPlaces(anchorNode: AnchorNode) {
        val currentLocation = currentLocation
        if (currentLocation == null) {
            Log.w(TAG, "Location has not been determined yet")
            return
        }

        val places = places
        if (places == null) {
            Log.w(TAG, "No places to put")
            return
        }

        for (place in places) {
            // Add the place in AR
            val placeNode = PlaceNode(this)
            placeNode.setParent(anchorNode)
            placeNode.localPosition =
                place.getPositionVector(orientationAngles[0], currentLocation.latLng)
            Log.d(TAG, "${place.getPositionVector(orientationAngles[0], currentLocation.latLng)}")
            placeNode.setOnTapListener { _, _ ->
                // showInfoWindow(place)
            }
            Log.d(TAG, "Place inserted!, ${place.geoLocation.lat}")
        }
    }

}

val Location.latLng: LatLng
    get() = LatLng(this.latitude, this.longitude)