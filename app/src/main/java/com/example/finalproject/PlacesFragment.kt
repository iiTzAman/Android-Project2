package com.example.finalproject
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class PlacesFragment : Fragment() {

    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_places, container, false)
        recycler = rootView.findViewById(R.id.recyclerView)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        fetchNearbyPlaces()
        return rootView
    }
    private fun fetchNearbyPlaces() {
        GlobalScope.launch(Dispatchers.IO) {
            val lat = arguments?.getString("lat")
            val lon = arguments?.getString("long")
            val apiKey = "AIzaSyDg0NyQJ4tES9a89a8hre15cTPaCTEIF0I"
            val radius = 1000
            val url =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lon&radius=$radius&type=restaurant&key=$apiKey"
            val connection = URL(url).openConnection() as HttpURLConnection
            val response = connection.inputStream.bufferedReader().use(BufferedReader::readText)

            val placesList = parsePlacesResponse(response)
            withContext(Dispatchers.Main) {
                val adapter = PlacesAdapter(placesList)
                recycler.layoutManager = LinearLayoutManager(requireContext())
                recycler.adapter = adapter
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
