package com.test.btest.adapters.http

import com.fasterxml.jackson.annotation.JsonProperty

data class ExtractDto(val phone: Int, val person: String)
data class ReturnDto(val phone: Int)

data class FonoapiData(
    @get:JsonProperty("DeviceName") val device: String,
    @get:JsonProperty("Brand") val brand: String,
    @get:JsonProperty("technology") val technology: String,
    @get:JsonProperty("_2g_bands") val bands2g: String,
    @get:JsonProperty("_3g_bands") val bands3g: String,
    @get:JsonProperty("_4g_bands") val bands4g: String
)