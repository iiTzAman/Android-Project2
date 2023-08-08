package com.example.finalproject

import com.google.android.gms.maps.model.LatLng

interface DataPassListener {
    fun onDataPassed(data: LatLng)
}