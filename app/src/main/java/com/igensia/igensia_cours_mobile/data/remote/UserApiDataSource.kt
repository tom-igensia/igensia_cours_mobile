package com.igensia.igensia_cours_mobile.data.remote

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

@Serializable
data class CoordDTO(
    val phone: String?,
    val mail: String?
)

@Serializable
data class UserDTO(
    val name: String?,
    val age: Int?,
    val coord: CoordDTO?
)

suspend fun main() {
    // Test fake user ou appel API
    val user = UserApiDataSource.loadUser()
    println("Il s'appelle ${user.name} pour le contacter : Phone : ${user.coord?.phone ?: "-"} - Mail : ${user.coord?.mail ?: "-"}")

    // Test collection
    val users = UserApiDataSource.loadUsers()
    println("Nombre d'utilisateurs récupérés : ${users.size}")

    // Fermeture du client pour arrêter le programme
    UserApiDataSource.close()
}

object UserApiDataSource {
    private const val API_URL = "https://www.amonteiro.fr/api/randomuser"

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

    suspend fun loadUser(): UserDTO {
        // Option 1 : Fake user initial
        // return UserDTO(name = "Johnny", age = 18, coord = CoordDTO(phone = "0606060606", mail = "aaa@bbb.fr"))

        // Option 2 : Appel API Ktor
        val response = client.get(API_URL)
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body<UserDTO>()
    }

    suspend fun loadUsers(): List<UserDTO> {
        val response = client.get("https://www.amonteiro.fr/api/randomusers")
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body<List<UserDTO>>()
    }

    fun close() = client.close()
}
