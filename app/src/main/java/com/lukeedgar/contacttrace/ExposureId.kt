package com.lukeedgar.contacttrace

import com.google.gson.annotations.SerializedName
import java.util.*

data class ExposureId(
    @SerializedName("id") val id : String,
    @SerializedName("dateOfGeneration") val dateOfGeneration : String)