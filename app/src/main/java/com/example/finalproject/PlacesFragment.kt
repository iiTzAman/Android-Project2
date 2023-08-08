package com.example.finalproject
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.PlacesAdapter
import com.example.finalproject.Place
import com.example.finalproject.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.log

class PlacesFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_places, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fetchNearbyPlaces()
        return rootView
    }

    private fun fetchNearbyPlaces() {
        GlobalScope.launch(Dispatchers.IO) {
            val latitude = "51.494"
            val longitude = "0.130"
            val apiKey = "AIzaSyDg0NyQJ4tES9a89a8hre15cTPaCTEIF0I"
            val radius = 1000
            val url =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latitude,$longitude&radius=$radius&type=restaurant&key=$apiKey"
            Log.d("Lat is here", "$latitude")
            val connection = URL(url).openConnection() as HttpURLConnection
            val response = connection.inputStream.bufferedReader().use(BufferedReader::readText)

            val placesList = parsePlacesResponse(response)
            withContext(Dispatchers.Main) {
                val adapter = PlacesAdapter(placesList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }
        }
    }

    private fun parsePlacesResponse(response: String): List<Place> {
        val placesList = mutableListOf<Place>()
        val jsonObject = JSONObject(response)
        val results = jsonObject.getJSONArray("results")
        for (i in 0 until results.length()) {
            val placeObject = results.getJSONObject(i)
            val name = placeObject.getString("name")
            val address = placeObject.optString("vicinity", "Address not available")
            val latLngObject = placeObject.getJSONObject("geometry").getJSONObject("location")
            val latitude = latLngObject.getDouble("lat")
            val longitude = latLngObject.getDouble("lng")
            placesList.add(Place(name, address, latitude, longitude))
        }
        return placesList
    }
}
