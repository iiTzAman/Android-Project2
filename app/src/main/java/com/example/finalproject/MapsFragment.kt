package com.example.finalproject
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


class MapsFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var lastestLocation: Location
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var dataPassListener: DataPassListener? = null

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataPassListener) {
            dataPassListener = context
        } else {
            throw RuntimeException("$context must implement DataPassListener")
        }
    }

    private fun sendDataToActivity(data: latLngAddress) {
        dataPassListener?.onDataPassed(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    lastestLocation = location
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLong)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

    }

    data class latLngAddress(val latitude: Double, val longitude: Double, val address: String?)


    private fun placeMarkerOnMap(currentLatLng: LatLng){
        val addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1)
        val addressText = if (addresses?.isNotEmpty() == true) {
            addresses?.get(0)?.getAddressLine(0)
        } else {
            "Address not available"
        }
        val markerOption = MarkerOptions().position(currentLatLng)
        val emailDetails = latLngAddress(currentLatLng.latitude, currentLatLng.longitude, addressText)
        sendDataToActivity(emailDetails)
        markerOption.title("$addressText")
        markerOption.snippet("Lat: %.4f, Lng: %.4f".format(currentLatLng.latitude, currentLatLng.longitude))
        googleMap.addMarker(markerOption)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
