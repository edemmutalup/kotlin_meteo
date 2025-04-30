package kotlin_meteo

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json


object OpenMeteoClient : WeatherAPI {
    private val client = HttpClient(CIO) {
        engine {
            requestTimeout = 10_000
            endpoint {
                connectTimeout = 5_000
                socketTimeout = 10_000
            }
        }
    }

    private fun URLBuilderCurrent(
        latitude: Double,
        longitude: Double
    ): String {
        return "https://api.open-meteo.com/v1/forecast?" +
                "latitude=$latitude&longitude=$longitude&" +
                "current=temperature_2m"
    }

    private fun URLBuilderRange(
        latitude: Double,
        longitude: Double,
        startDate: String,
        endDate: String
    ): String {
        return "https://api.open-meteo.com/v1/forecast?" +
                "latitude=$latitude&longitude=$longitude&" +
                "start_date=$startDate&end_date=$endDate&" +
                "hourly=temperature_2m"
    }

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): CurrentWeatherData {
        val URL: String = URLBuilderCurrent(latitude, longitude)
        return Json.decodeFromString<CurrentWeatherData>(client.get(URL).bodyAsText())
    }

    override suspend fun getWeatherRange(
        latitude: Double,
        longitude: Double,
        startDate: String,
        endDate: String
    ): WeatherRangeData {
        val URL: String = URLBuilderRange(latitude, longitude, startDate, endDate)
        return Json.decodeFromString<WeatherRangeData>(client.get(URL).bodyAsText())
    }
}