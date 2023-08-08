package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class SendEmailFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_send_email, container, false)
        emailEditText = rootView.findViewById(R.id.emailEditText)

        val lastEmailAddress = sharedPreferencesManager.getLastEmailAddress()
        emailEditText.setText(lastEmailAddress)

        rootView.findViewById<Button>(R.id.sendEmailButton).setOnClickListener {
            sendEmail()
        }

        return rootView
    }

    private fun sendEmail() {
        val lat = arguments?.getString("lat")
        val lon = arguments?.getString("long")
        val address = arguments?.getString("address")
        val emailAddress = emailEditText.text.toString()
        sharedPreferencesManager.saveLastEmailAddress(emailAddress)

        val emailSubject = "Location Information"
        val emailText = "Latitude: $lat\nLongitude: $lon\nAddress: $address"
        val mailtoUrl = "mailto:$emailAddress?subject=$emailSubject&body=$emailText"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(mailtoUrl)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No browser app installed", Toast.LENGTH_SHORT).show()
        }
    }
}
