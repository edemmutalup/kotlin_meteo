package kotlin_meteo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyUnits(
    val time: String,
    @SerialName("temperature_2m") val temperature2M: String
)

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature2M: List<Double>
)

@Serializable
data class WeatherRangeData(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("hourly_units") val hourlyUnits: HourlyUnits,
    @SerialName("hourly") val hourly: HourlyData
)


data class MinMaxData(
    val min: Pair<Double, String>,
    val max: Pair<Double, String>
)

fun WeatherRangeData.calcMinMax(): MinMaxData {
    val len = this.hourly.time.size
    val time = this.hourly.time
    val temp = this.hourly.temperature2M
    var min: Pair<Double, String> = Pair(temp[0], time[0])
    var max: Pair<Double, String> = Pair(temp[0], time[0])
    for (i in 0 until len) {
        if (min.first > temp[i]) {
            min = Pair(temp[i], time[i])
        }
        if (max.first < temp[i]) {
            max = Pair(temp[i], time[i])
        }
    }
    return MinMaxData(min, max)
}

