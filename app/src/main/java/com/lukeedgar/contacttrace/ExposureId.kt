package com.lukeedgar.contacttrace

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


data class ExposureId(
    @SerializedName("id") val id: String,
    @SerializedName("dateOfGeneration") val dateOfGeneration: String
) {

    val isInInfectiousPeriod: Boolean by lazy {
        // 14 day infectious period.
        val hoursInInfectiousPeriod = 336
        val dateTime: ZonedDateTime = Instant.ofEpochMilli(dateOfGeneration.toLong())
            .atZone(ZoneId.systemDefault())
        dateTime > dateTime.minusHours(336)
    }
}