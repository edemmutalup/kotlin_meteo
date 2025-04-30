package kotlin_meteo

import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import tornadofx.Controller

class WeatherController : Controller(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext = Dispatchers.JavaFx + job

    fun loadCurrentWeather(
        lat: Double,
        lon: Double,
        onResult: (String) -> Unit
    ) {
        launch {
            val text = withContext(Dispatchers.IO) {
                val weather = OpenMeteoClient.getCurrentWeather(lat,lon)
                val temperature = weather.current.temperature2M
                "Текущая температура для $lat, $lon: $temperature °C"
            }
            onResult(text)
        }
    }

    fun loadWeatherRange(
        lat: Double,
        lon: Double,
        from: String,
        to: String,
        onResult: (String) -> Unit
    ) {
        launch {
            val text = withContext(Dispatchers.IO) {
                val weather = OpenMeteoClient.getWeatherRange(lat, lon, from, to)
                val result = weather.calcMinMax()
                "Погода для $lat, $lon c $from по $to \n" +
                        "Мин. температура ${result.min.first} °C достигается ${result.min.second} \n" +
                        "Макс. температура ${result.max.first} °C достигается ${result.max.second}"
            }
            onResult(text)
        }
    }

    fun cancelAllJobs() {
        job.cancel()
    }
}