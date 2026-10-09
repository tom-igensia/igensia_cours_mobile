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

    fun loadWeathers(cityName: String) {
        runInProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataList.value = WeatherApiDataSource.loadWeathers(cityName)
            } finally {
                runInProgress.value = false
            }
        }
    }
}

suspend fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Nice")

    while (viewModel.runInProgress.value) {
        delay(500)
    }

    // Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")

    // Pour que le programme s'arrête, inutile sur Android
    WeatherApiDataSource.close()
}

