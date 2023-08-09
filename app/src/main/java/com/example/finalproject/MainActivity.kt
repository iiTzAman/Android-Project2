package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity(), DataPassListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var currentLat: String
    private lateinit var currentLong: String
    private lateinit var currentAddress: String
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferencesManager = SharedPreferencesManager(this)
        replaceFragment(MapsFragment())
        Places.initialize(applicationContext, "AIzaSyDg0NyQJ4tES9a89a8hre15cTPaCTEIF0I")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.google_maps_menu -> {
                replaceFragment(MapsFragment())
                true
            }
            R.id.google_places_menu -> {
                val fragment = PlacesFragment()
                val bundle = Bundle()
                bundle.putString("lat", currentLat)
                bundle.putString("long", currentLong)
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
                true
            }
            R.id.email_menu -> {
                val fragment = SendEmailFragment()
                val bundle = Bundle()
                bundle.putString("lat", currentLat)
                bundle.putString("long", currentLong)
                bundle.putString("address", currentAddress)
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
            }
            R.id.about_menu -> {
                val fragment = AboutFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDataPassed(data: MapsFragment.latLngAddress) {
        Log.d("Received LatLNG","$data")
        currentLat = data.latitude.toString()
        currentLong = data.longitude.toString()
        currentAddress = data.address.toString()
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}