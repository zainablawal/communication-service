package com.babbangona.communicationmicroservice.api

import com.babbangona.communicationmicroservice.CommunicationServiceRequest
import com.babbangona.communicationmicroservice.CommunicationServiceResponse
import retrofit2.Call
import retrofit2.http.*

interface CommunicationServiceInterfaceApi {

    @Headers("Content-Type: application/json")
    @POST("v1/message")
    fun sendShortMessageService(
        @Body communicationServiceRequest: CommunicationServiceRequest
    ): Call<CommunicationServiceResponse>


}
