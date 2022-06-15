package com.babbangona.communicationmicroservice.api

import android.util.Log
import com.babbangona.communicationmicroservice.CommunicationServiceRequest
import com.babbangona.communicationmicroservice.CommunicationServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunicationServiceRestApi {

    fun sendShortMessageService(
        communicationServiceRequest: CommunicationServiceRequest,
        onResult: (CommunicationServiceResponse?) -> Unit
    ) {
        val retrofit =
            CommunicationServiceBuilder.buildService(CommunicationServiceInterfaceApi::class.java)
        retrofit.sendShortMessageService(communicationServiceRequest).enqueue(
            object : Callback<CommunicationServiceResponse> {
                override fun onFailure(call: Call<CommunicationServiceResponse>, t: Throwable) {
                    Log.d("testttt", "onFailure: ")
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<CommunicationServiceResponse>,
                    response: Response<CommunicationServiceResponse>
                ) {
                    val sendShortMessageService = response.body()
                    onResult(sendShortMessageService)
                    Log.d("testttt", "onResponse: ")
                }
            }
        )
    }
}