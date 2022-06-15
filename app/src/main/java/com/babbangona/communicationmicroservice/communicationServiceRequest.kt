package com.babbangona.communicationmicroservice

import com.google.gson.annotations.SerializedName

data class CommunicationServiceRequest(
    @SerializedName("message") val message: String?,
    @SerializedName("number") val number: String?
)
