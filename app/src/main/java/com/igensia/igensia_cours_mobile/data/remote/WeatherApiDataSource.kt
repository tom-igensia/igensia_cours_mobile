package com.igensia.igensia_cours_mobile.data.remote

import com.igensia.igensia_cours_mobile.domain.model.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.Scanner

// 5 DTOs nécessaires pour exploiter le JSON OpenWeatherMap
@Serializable
data class WeatherResponseDTO(
    val list: List<CityWeatherDTO>?
)

@Serializable
data class CityWeatherDTO(
    val id: Int?,
    val name: String?,
    val main: MainDTO?,
    val wind: WindDTO?,
    val weather: List<WeatherConditionDTO>?
)

@Serializable
data class MainDTO(
    val temp: Double?
)

@Serializable
data class WindDTO(
    val speed: Double?
)

@Serializable
data class WeatherConditionDTO(
    val description: String?,
    val icon: String?
)

suspend fun main() {
    // 1. Affichage des résumés pour la ville de Nice
    println("--- Météo pour Nice ---")
    val weathersNice = WeatherApiDataSource.loadWeathers("Nice")
    for (weather in weathersNice) {
        println(weather.getResume())
        println("-------------------")
    }

    // 2. Pour les plus rapides : Demander une ville à l'utilisateur avec un Scanner
    val scanner = Scanner(System.`in`)
    print("Entrez le nom d'une ville : ")
    if (scanner.hasNextLine()) {
        val cityName = scanner.nextLine()
        if (cityName.isNotBlank()) {
            println("--- Météo pour $cityName ---")
            try {
                val weathersCity = WeatherApiDataSource.loadWeathers(cityName)
                if (weathersCity.isEmpty()) {
                    println("Aucune météo trouvée pour $cityName.")
                } else {
                    for (weather in weathersCity) {
                        println(weather.getResume())
                        println("-------------------")
                    }
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération de la météo : ${e.message}")
            }
        }
    }

    WeatherApiDataSource.close()
}

object WeatherApiDataSource {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    private val client = HttpClient {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }

    // Retourne la liste des points météos de la ville en paramètre
    suspend fun loadWeathers(cityName: String): List<Weather> {
        val response = client.get("$BASE_URL$cityName")
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        val responseDTO = response.body<WeatherResponseDTO>()

        return responseDTO.list?.map { cityWeather ->
            val iconCode = cityWeather.weather?.firstOrNull()?.icon ?: ""
            val iconUrl = if (iconCode.isNotBlank()) "https://openweathermap.org/img/wn/${iconCode}@4x.png" else ""
            Weather(
                id = cityWeather.id ?: 0,
                name = cityWeather.name ?: "",
                temp = cityWeather.main?.temp ?: 0.0,
                speed = cityWeather.wind?.speed ?: 0.0,
                description = cityWeather.weather?.firstOrNull()?.description ?: "",
                icon = iconUrl
            )
        } ?: emptyList()
    }

    fun close() = client.close()
}
