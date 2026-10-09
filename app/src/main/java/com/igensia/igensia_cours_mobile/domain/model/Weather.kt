package com.igensia.igensia_cours_mobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    var id: Int, // id d'un point météo
    var name: String,
    var temp: Double, // Température
    var speed: Double, // Vitesse du vent
    var description: String, // 1er description
    var icon: String // 1er icone
) {
    fun getResume(): String {
        return "Il fait $temp° à $name (id=$id) avec un vent de $speed m/s\n-Description : $description\n-Icône : $icon"
    }
}
