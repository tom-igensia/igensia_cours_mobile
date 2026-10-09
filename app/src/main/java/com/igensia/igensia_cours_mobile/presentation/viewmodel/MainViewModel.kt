package com.igensia.igensia_cours_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igensia.igensia_cours_mobile.data.remote.WeatherApiDataSource
import com.igensia.igensia_cours_mobile.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<Weather>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {
        println("Instanciation de MainViewModel")
        loadFakeData()
    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            Weather(
                id = 1,
                name = "Paris",
                temp = 18.5,
                description = "ciel dégagé",
                icon = "https://picsum.photos/200",
                speed = 5.0
            ),
            Weather(
                id = 2,
                name = "Toulouse",
                temp = 22.3,
                description = "partiellement nuageux",
                icon = "https://picsum.photos/201",
                speed = 3.2
            ),
            Weather(
                id = 3,
                name = "Toulon",
                temp = 25.1,
                description = "ensoleillé",
                icon = "https://picsum.photos/202",
                speed = 6.7
            ),
            Weather(
                id = 4,
                name = "Lyon",
                temp = 19.8,
                description = "pluie légère",
                icon = "https://picsum.photos/203",
                speed = 4.5
            )
        ).shuffled()
    }

    fun loadWeathers(cityName: String?) {
        runInProgress.value = true
        errorMessage.value = ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cityName.isNullOrBlank() || cityName.length < 3) {
                    throw IllegalArgumentException("Le nom de la ville doit contenir au moins 3 caractères")
                }
                dataList.value = WeatherApiDataSource.loadWeathers(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Erreur inconnue"
            } finally {
                runInProgress.value = false
            }
        }
    }
}

suspend fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("")
    //viewModel.loadWeathers("Paris")

    while (viewModel.runInProgress.value) {
        delay(500)
    }

    // Affichage de la liste et du message d'erreur
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}")

    // Pour que le programme s'arrête, inutile sur Android
    WeatherApiDataSource.close()
}
