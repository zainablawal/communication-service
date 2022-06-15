package com.babbangona.communicationmicroservice

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.babbangona.communicationmicroservice.api.CommunicationServiceRestApi
import com.babbangona.communicationmicroservice.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var message: String = ""
    private lateinit var phoneNumbers: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if (validateUserInput()) {
                if (checkForInternet(this)) {
                    sendShortMessagingService()
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.dialogTitle)
                    builder.setMessage(getString(R.string.dialogMessage, phoneNumbers.toString()))
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                        binding.editTextMessage.text?.clear()
                        binding.editTextPhone.text?.clear()

                    }
                    builder.setCancelable(false)
                    builder.show()
                } else {
                    Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }


    private fun validateUserInput(): Boolean {
        message = binding.editTextMessage.text.toString()
        val listOfPhoneNumbers = binding.editTextPhone.text.toString()
        phoneNumbers = separateNumbers(listOfPhoneNumbers)

        for (phoneNumber in phoneNumbers) {
            if (!phoneNumber.startsWith("+234")) {
                Toast.makeText(
                    applicationContext,
                    "Phone Number should start with +234",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (phoneNumber.length != 14) {
                Toast.makeText(
                    applicationContext,
                    "Phone Number should contain 14 characters",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        if (message.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Enter your message",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun sendShortMessagingService() {
        val apiService = CommunicationServiceRestApi()
        for (phoneNumber in phoneNumbers) {
            val communicationServiceRequest = CommunicationServiceRequest(message, phoneNumber)
            apiService.sendShortMessageService(communicationServiceRequest) {
                Log.d("testttt", "onResponse: " + it.toString())
                if (it?.status == "success") {
                    Toast.makeText(
                        applicationContext,
                        "Message successfully sent to $phoneNumber",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Message sending to $phoneNumber failed",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun separateNumbers(listOfPhoneNumbers: String): List<String> {
        val phoneNumbers = listOfPhoneNumbers.split(",")
        var formattedNumbers: ArrayList<String> = arrayListOf()
        for (number in phoneNumbers) {
            formattedNumbers.add(number.trim())
        }
        return formattedNumbers
    }

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}