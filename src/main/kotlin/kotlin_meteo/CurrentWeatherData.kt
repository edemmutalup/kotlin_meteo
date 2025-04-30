package kotlin_meteo

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Current(
    val time: String,
    val interval: Int,
    @SerialName("temperature_2m") val temperature2M: Double
)

@Serializable
data class CurrentUnits(
    val time: String,
    val interval: String,
    @SerialName("temperature_2m") val temperature2M: String
)

@Serializable
data class CurrentWeatherData(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("current_units") val currentUnits: CurrentUnits,
    val current: Current

)
