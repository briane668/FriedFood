package com.wade.friedfood.sample

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

//class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
//
//
//    //    等於空??
//    private var map: GoogleMap? = null
//    private var cameraPosition: CameraPosition? = null
//
//    // The entry point to the Places API.
//    private lateinit var placesClient: PlacesClient
//
//    // The entry point to the Fused Location Provider.
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//
//    // A default location (Sydney, Australia) and default zoom to use when location permission is
//    // not granted.
//    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
//    private var locationPermissionGranted = false
//
//    // The geographical location where the device is currently located. That is, the last-known
//    // location retrieved by the Fused Location Provider.
//    private var lastKnownLocation: Location? = null
//    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
//    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
//    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
//    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)
////    private lateinit var mMap: GoogleMap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////儲存目前位子
//        if (savedInstanceState != null) {
//            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
//            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
//        }
//        Places.initialize(applicationContext, getString(R.string.maps_api_key))
//        placesClient = Places.createClient(this)
//
//        // Construct a FusedLocationProviderClient.
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//
//
//
//
//        setContentView(R.layout.activity_maps)
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        //        mMap = googleMap
//        this.map = googleMap
//        this.map!!.setOnMarkerClickListener(this)
//
//
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        val sydney2 = LatLng(-35.8523341, 181.2106085)
//
//        val mymarker = map!!.addMarker(
//            MarkerOptions().position(sydney).title("Marker in Sydney").snippet("i am here")
//        )
//        map!!.addMarker(MarkerOptions().position(sydney2).title("Marker"))
////        點下會顯示文字
//        map!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//
//        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
//            // Return null here, so that getInfoContents() is called next.
//            override fun getInfoWindow(arg0: Marker): View? {
//                return null
//            }
//
//            override fun getInfoContents(marker: Marker): View {
//                // Inflate the layouts for the info window, title and snippet.
//                val infoWindow = layoutInflater.inflate(
//                    R.layout.custom_info_contents,
//                    findViewById<FrameLayout>(R.id.map), false
//                )
//                val title = infoWindow.findViewById<TextView>(R.id.title)
//                title.text = marker.title
//                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
//                snippet.text = marker.snippet
//                return infoWindow
//            }
//        })
//
//
//        // Prompt the user for permission.
////        要求位置權限，出現右上角的定位功能
//        getLocationPermission()
//        // Do other setup activities here too, as described elsewhere in this tutorial.
//
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();
//    }
//
//
//    private val COLOR_BLACK_ARGB = -0x1000000
//    private val POLYLINE_STROKE_WIDTH_PX = 12
//
//
//    private fun stylePolyline(polyline: Polyline) {
//        // Get the data object stored with the polyline.
//        val type = polyline.tag?.toString() ?: ""
//        when (type) {
//            "A" -> {
//                // Use a custom bitmap as the cap at the start of the line.
//                polyline.startCap = CustomCap(
//                    BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10f
//                )
//            }
//            "B" -> {
//                // Use a round cap at the start of the line.
//                polyline.startCap = RoundCap()
//            }
//        }
//        polyline.endCap = RoundCap()
//        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
//        polyline.color = COLOR_BLACK_ARGB
//        polyline.jointType = JointType.ROUND
//    }
//
//
//    /**
//     * Saves the state of the map when the activity is paused.
//     */
//    // [START maps_current_place_on_save_instance_state]
//    override fun onSaveInstanceState(outState: Bundle) {
//        map?.let { map ->
//            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
//            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
//        }
//        super.onSaveInstanceState(outState)
//    }
//
//
//    /**
//     * Sets up the options menu.
//     * @param menu The options menu.
//     * @return Boolean.
//     */
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.current_place_menu, menu)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.option_get_place) {
//            showCurrentPlace()
//        }
//        return true
//    }
//
//
//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    // [START maps_current_place_show_current_place]
//    private fun showCurrentPlace() {
//        if (map == null) {
//            return
//        }
//        if (locationPermissionGranted) {
//            // Use fields to define the data types to return.
//            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//
//            // Use the builder to create a FindCurrentPlaceRequest.
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            val placeResult = placesClient.findCurrentPlace(request)
//            placeResult.addOnCompleteListener { task ->
//                if (task.isSuccessful && task.result != null) {
//                    val likelyPlaces = task.result
//
//                    // Set the count, handling cases where less than 5 entries are returned.
//                    val count =
//                        if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
//                            likelyPlaces.placeLikelihoods.size
//                        } else {
//                            M_MAX_ENTRIES
//                        }
//                    var i = 0
//                    likelyPlaceNames = arrayOfNulls(count)
//                    likelyPlaceAddresses = arrayOfNulls(count)
//                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
//                    likelyPlaceLatLngs = arrayOfNulls(count)
//                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
//                        // Build a list of likely places to show the user.
//                        likelyPlaceNames[i] = placeLikelihood.place.name
//                        likelyPlaceAddresses[i] = placeLikelihood.place.address
//                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
//                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
//                        i++
//                        if (i > count - 1) {
//                            break
//                        }
//                    }
//
//                    // Show a dialog offering the user the list of likely places, and add a
//                    // marker at the selected place.
//                    openPlacesDialog()
//                } else {
//                    Log.e(TAG, "Exception: %s", task.exception)
//                }
//            }
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.")
//
//            // Add a default marker, because the user hasn't selected a place.
//            map?.addMarker(
//                MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(defaultLocation)
//                    .snippet(getString(R.string.default_info_snippet))
//            )
//
//            // Prompt the user for permission.
//            getLocationPermission()
//        }
//    }
//
//
//    override fun onMarkerClick(marker: Marker?): Boolean {
//        // TODO Auto-generated method stub
//        // googleMap.clear();
//
//
//        Toast.makeText(
//            applicationContext, "USER MARKER",
//            Toast.LENGTH_LONG
//        ).show()
//        return true
//    }
//
//
//    private fun openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        val listener =
//            DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
//                val markerLatLng = likelyPlaceLatLngs[which]
//                var markerSnippet = likelyPlaceAddresses[which]
//                if (likelyPlaceAttributions[which] != null) {
//                    markerSnippet = """
//                    $markerSnippet
//                    ${likelyPlaceAttributions[which]}
//                    """.trimIndent()
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                map?.addMarker(
//                    MarkerOptions()
//                        .title(likelyPlaceNames[which])
//                        .position(markerLatLng!!)
//                        .snippet(markerSnippet)
//                )
//
//                // Position the map's camera at the location of the marker.
//                map?.moveCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        markerLatLng,
//                        DEFAULT_ZOOM.toFloat()
//                    )
//                )
//            }
//
//        // Display the dialog.
//        AlertDialog.Builder(this)
//            .setTitle(R.string.pick_place)
//            .setItems(likelyPlaceNames, listener)
//            .show()
//    }
//
//
//    /**
//     * Prompts the user for permission to use the device location.
//     */
//    // [START maps_current_place_location_permission]
//    private fun getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            locationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//
//    private fun updateLocationUI() {
//        if (map == null) {
//            return
//        }
//        try {
//            if (locationPermissionGranted) {
//                map?.isMyLocationEnabled = true
//                map?.uiSettings?.isMyLocationButtonEnabled = true
//            } else {
//                map?.isMyLocationEnabled = false
//                map?.uiSettings?.isMyLocationButtonEnabled = false
//                lastKnownLocation = null
//                getLocationPermission()
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
//    // [END maps_current_place_update_location_ui]
//
//    companion object {
//        private val TAG = MapsActivity::class.java.simpleName
//        private const val DEFAULT_ZOOM = 15
//        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//
//        // Keys for storing activity state.
//        // [START maps_current_place_state_keys]
//        private const val KEY_CAMERA_POSITION = "camera_position"
//        private const val KEY_LOCATION = "location"
//        // [END maps_current_place_state_keys]
//
//        // Used for selecting the current place.
//        private const val M_MAX_ENTRIES = 5
//    }
//
//    //    取得 Android 裝置的位置並設定地圖位置
//    private fun getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (locationPermissionGranted) {
//                val locationResult = fusedLocationProviderClient.lastLocation
//                locationResult.addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Set the map's camera position to the current location of the device.
//                        lastKnownLocation = task.result
//                        if (lastKnownLocation != null) {
//                            map?.moveCamera(
//                                CameraUpdateFactory.newLatLngZoom(
//                                    LatLng(
//                                        lastKnownLocation!!.latitude,
//                                        lastKnownLocation!!.longitude
//                                    ), DEFAULT_ZOOM.toFloat()
//                                )
//                            )
//                        }
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.")
//                        Log.e(TAG, "Exception: %s", task.exception)
//                        map?.moveCamera(
//                            CameraUpdateFactory
//                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
//                        )
//                        map?.uiSettings?.isMyLocationButtonEnabled = false
//                    }
//                }
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message, e)
//        }
//    }
//
//
//
//
//
//
//
//
//}
