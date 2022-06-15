package com.babbangona.communicationmicroservice

import com.google.gson.annotations.SerializedName

data class CommunicationServiceResponse(
    @SerializedName("status") val status: String?
)