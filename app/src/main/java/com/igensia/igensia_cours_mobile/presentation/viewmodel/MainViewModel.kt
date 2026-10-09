package com.igensia.igensia_cours_mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.igensia.igensia_cours_mobile.data.remote.WeatherApiDataSource
import com.igensia.igensia_cours_mobile.domain.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    // MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<Weather>())

    suspend fun loadWeathers(cityName: String) {
        // TODO récupérer des données et les mettre dans dataList
        dataList.value = WeatherApiDataSource.loadWeathers(cityName)
    }
}

suspend fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Nice")
    // Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")

    // Pour que le programme s'arrête, inutile sur Android
    WeatherApiDataSource.close()
}
