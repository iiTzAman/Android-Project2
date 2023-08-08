package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var currentLatitude: String
    private lateinit var currentLongitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                replaceFragment(PlacesFragment())
                true
            }
            R.id.email_menu -> Toast.makeText(this, "Maps Selected", Toast.LENGTH_SHORT).show()
            R.id.about_menu -> Toast.makeText(this, "Maps Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}