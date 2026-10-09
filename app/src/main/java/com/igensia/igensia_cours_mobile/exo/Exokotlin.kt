package com.igensia.igensia_cours_mobile.exo

import kotlin.random.Random

@JvmOverloads
fun boulangerie(
    croissants: Int = 0,
    baguettes: Int = 0,
    sandwiches: Int = 0
): Double = croissants * PRIX_CROISSANT + baguettes * PRIX_BAGUETTE + sandwiches * PRIX_SANDWICH

fun main() {
    println("Hello World")

    // 1. Variable v1 non-nullable
    val v1: String = "toto"
    println(v1.uppercase())
    // v1 = null // Erreur de compilation : Null can not be a value of a non-null type String

    // 2. Variable v2 nullable avec safe call
    val v2: String? = "toto"
    println(v2?.uppercase())

    // 3. Variable v3 nullable égale à null
    val v3: String? = null
    println(v3?.uppercase())

    // 4. Variable v4 et opérateur Elvis
    var v4 : Int? = null
    if(Random.nextBoolean()){
        v4 = Random.nextInt(10)
    }
    println(v4 ?: "Pas de valeur")

    // - Typage de v3 + v3 : String? (car la concaténation avec des String? retourne un String? qui vaut "nullnull" si v3 est null)
    val v3PlusV3: String? = v3 + v3
    println(v3PlusV3)

    // - If si v3 est nulle, vide ou ne contient que des espaces
    if (v3.isNullOrBlank()) {
        println("Toto")
    }

    // Appels de boulangerie avec paramètres nommés :
    println("2 croissants : ${boulangerie(croissants = 2)} €")
    println("2 baguettes et 3 sandwiches : ${boulangerie(baguettes = 2, sandwiches = 3)} €")
}
