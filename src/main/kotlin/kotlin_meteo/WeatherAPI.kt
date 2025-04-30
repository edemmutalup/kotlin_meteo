package kotlin_meteo

interface WeatherAPI {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherData
    suspend fun getWeatherRange(latitude: Double, longitude: Double, startDate: String, endDate: String): WeatherRangeData
}