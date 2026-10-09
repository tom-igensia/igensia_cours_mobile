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
    val errorMessage = MutableStateFlow<String?>(null)

    fun loadWeathers(cityName: String?) {
        runInProgress.value = true
        errorMessage.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (cityName.isNullOrBlank() || cityName.length < 3) {
                    throw IllegalArgumentException("Le nom de la ville doit contenir au moins 3 caractères")
                }
                dataList.value = WeatherApiDataSource.loadWeathers(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message
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


