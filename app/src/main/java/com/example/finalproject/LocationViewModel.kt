package com.example.finalproject

import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var address: String = ""
}