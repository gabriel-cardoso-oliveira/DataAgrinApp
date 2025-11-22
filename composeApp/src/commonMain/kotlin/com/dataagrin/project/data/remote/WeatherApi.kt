package com.dataagrin.project.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class WeatherResponse(
    val current: CurrentWeather,
    val hourly: HourlyWeather
)

@Serializable
data class CurrentWeather(
    val temperature_2m: Double,
    val relative_humidity_2m: Double,
    val weather_code: Int
)

@Serializable
data class HourlyWeather(
    val time: List<String>,
    val temperature_2m: List<Double>
)

class WeatherApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse {
        return client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", lat)
            parameter("longitude", lon)
            parameter("current", "temperature_2m,relative_humidity_2m,weather_code")
            parameter("hourly", "temperature_2m")
            parameter("forecast_days", 1)
        }.body()
    }
}
