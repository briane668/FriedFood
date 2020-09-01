package com.wade.friedfood.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wade.friedfood.ext.hideKeyboard
import com.wade.friedfood.ext.toParcelableShop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.wade.friedfood.NavigationDirections
import com.wade.friedfood.R
import com.wade.friedfood.databinding.FragmentMapsBinding
import com.wade.friedfood.ext.getVmFactory
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerClickListener {


    private val viewModel by viewModels<MapViewModel> { getVmFactory() }

    lateinit var binding: FragmentMapsBinding

    var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(24.972569, 121.517274)


    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null


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

        viewModel.getShops()

        this.map?.isMyLocationEnabled = true

        this.map = googleMap

        this.map!!.setOnMarkerClickListener(this)

        this.map?.setOnInfoWindowClickListener(this)

        this.map!!.setMinZoomPreference(15.0f)


        viewModel.shop.observe(viewLifecycleOwner, Observer {
            viewModel.calculateDistance(it)
        })

        viewModel.naerShop.observe(viewLifecycleOwner, Observer {
            viewModel.naerShop.value.let { shops ->
                if (shops != null) {
                    for (shop in shops) {

                        viewModel.coroutineScope.launch {
                            val rating = viewModel.getRatingByShop(shop)
                            val x = shop.location?.latitude
                            val y = shop.location?.longitude
                            val sydney = y?.let { it1 -> x?.let { it2 -> LatLng(it2, it1) } }
                            map!!.addMarker(sydney?.let { it1 ->
                                MarkerOptions().position(it1).title(shop.name)
                                    .snippet("評價${rating}顆星")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                            })
                        }
                    }
                }
            }
        })


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


        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    override fun onResume() {
        super.onResume()
        binding.viewMap.onResume();
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

        binding.viewMap.onCreate(savedInstanceState)

        binding.mapView.adapter = MapAdapter(MapAdapter.OnClickListener {

            viewModel.displayShopDetails(it)
        }, viewModel)

        viewModel.navigateToSelectedShop.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        NavigationDirections.actionGlobalDetailFragment(
                            it.toParcelableShop()
                        )
                    )
                viewModel.displayShopDetailsComplete()
            }
        })

        getLocationPermission()


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        Places.initialize(requireContext(), getString(R.string.maps_api_key))
        placesClient = Places.createClient(requireContext())

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }



        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.editSearch.hideKeyboard()

                map?.clear()
                viewModel.naerShop.value = null
                binding.mapView.visibility = View.INVISIBLE
                viewModel.getMenu(v.text.toString())

            }
            return@setOnEditorActionListener false
        }


        binding.imageHomeClear.setOnClickListener {
            binding.editSearch.text.clear()
        }

        binding.refresh.setOnClickListener {
            this.map?.clear()
            viewModel.naerShop.value = null
            viewModel.getShops()
            binding.mapView.visibility = View.INVISIBLE
        }


        viewModel.menus.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                viewModel.searchShopByMenu(it)
            }
        })


        return binding.root

    }


    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {

            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_update_location_ui]

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 16
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

    }


    override fun onMarkerClick(marker: Marker?): Boolean {

        if (marker != null) {
            val markerName = marker.title
            binding.mapView.visibility = View.VISIBLE
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
        for (it in viewModel.shop.value!!) {
            if (marker.title == it.name) {
                findNavController().navigate(
                    NavigationDirections.actionGlobalDetailFragment(
                        it.toParcelableShop()
                    )
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
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
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            setupMap()

        } else {


            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun setupMap() {

        binding.viewMap.getMapAsync(callback)

    }


    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {

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
                                ),
                                DEFAULT_ZOOM.toFloat()
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

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    setupMap()

                } else {
                    return getLocationPermission()
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}