package com.wade.friedfood.map

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.wade.friedfood.NavigationDirections
import com.wade.friedfood.R
import com.wade.friedfood.databinding.FragmentMapsBinding
import com.wade.friedfood.detail.DetailFragmentArgs
import com.wade.friedfood.ext.getVmFactory
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerClickListener {


    private val viewModel by viewModels<MapViewModel> { getVmFactory() }

    lateinit var binding: FragmentMapsBinding

    private var marker1: Marker? = null
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(24.972569, 121.517274)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        this.map = googleMap
        this.map!!.setOnMarkerClickListener(this)
        this.map?.setOnInfoWindowClickListener(this)


//        viewModel.shop.observe(viewLifecycleOwner, Observer {
//            Log.d("Wade", "$it")
//        })



// 按下去後 call funtion 叫出資料 先clear 在畫出來
        viewModel.shop.observe(viewLifecycleOwner, Observer {
            viewModel.shop.value.let {
                if (it != null) {
                    for (i in it) {

                        val x = i.location?.latitude
                        val y = i.location?.longitude
                        val z = i.name
                        val sydney = y?.let { it1 -> x?.let { it2 -> LatLng(it2, it1) } }
                        map!!.addMarker(sydney?.let { it1 ->
                            MarkerOptions().position(it1).title(z).snippet("評價${i.star}顆星")
                        })
                    }
                }
            }
        })











        val sydney = LatLng(24.972569, 121.517274)

//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").snippet("i am here"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

//        marker1 = map!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").snippet("i am here"))


        map!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))




        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(
                    R.layout.custom_info_contents,
                    view_map, false
                )
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })


        // Prompt the user for permission.
//        要求位置權限，出現右上角的定位功能
        getLocationPermission()
        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_maps, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.mapView.adapter = MapAdapter(MapAdapter.OnClickListener {
            viewModel.displayShopDetails(it)
        })

        viewModel.navigateToSelectedShop.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                // Must find the NavController from the Fragment
                this.findNavController()
                    .navigate(NavigationDirections.actionGlobalDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayShopDetailsComplete()
            }
        })



        binding.viewMap.onCreate(savedInstanceState)
        binding.viewMap.onResume();

        binding.viewMap.getMapAsync(callback)


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        Places.initialize(requireContext(), getString(R.string.maps_api_key))
        placesClient = Places.createClient(requireContext())

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }





        binding.chicken.setOnClickListener {
            this.map?.clear()
            viewModel.shop.value=null
            viewModel.getFriedChicken()

        }


        binding.refresh.setOnClickListener {
            this.map?.clear()
            viewModel.shop.value=null
            viewModel.getShop()
        }


        viewModel.menus.observe(viewLifecycleOwner, Observer {
            viewModel.getSelectedShop(it)
        })








        return binding.root

    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_update_location_ui]

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        // googleMap.clear();
        if (marker != null) {
            val markerName = marker.title

            viewModel.shop.value?.let {
                for ((index, shop) in it.withIndex()) {
                    if (shop.name == markerName) {
                        binding.mapView.smoothScrollToPosition(index)
                        break
                    }
                }
            }
        }
        return false
    }


    override fun onInfoWindowClick(marker: Marker) {


        for (i in viewModel.shop.value!!) {
            if (marker.title == i.name) {

                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(i))

            }
        }

        Toast.makeText(context, "Click Info Window", Toast.LENGTH_SHORT).show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace()
        }
        return true
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count =
                        if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                            likelyPlaces.placeLikelihoods.size
                        } else {
                            M_MAX_ENTRIES
                        }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            map?.addMarker(
                MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet))
            )

            // Prompt the user for permission.
            getLocationPermission()
        }
    }


    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener =
            DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
                val markerLatLng = likelyPlaceLatLngs[which]
                var markerSnippet = likelyPlaceAddresses[which]
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map?.addMarker(
                    MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng!!)
                        .snippet(markerSnippet)
                )

                // Position the map's camera at the location of the marker.
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markerLatLng,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
            }

        // Display the dialog.
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
    }


    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    //    取得 Android 裝置的位置並設定地圖位置
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        MapViewModel.userPosition.value = lastKnownLocation
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


}