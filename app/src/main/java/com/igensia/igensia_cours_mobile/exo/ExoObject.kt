package com.igensia.igensia_cours_mobile.exo

data class CarEntity(val marque: String, val model: String) {
    val couleur: String = "grise"
}

class RandomName {
    private val names: ArrayList<String> = arrayListOf("Thomas", "Sarah", "Alex")
    private var lastSelected: String? = null

    fun add(name: String?): Boolean = !name.isNullOrBlank() && name !in names && names.add(name)

    fun addAll(vararg newNames: String) {
        newNames.forEach { add(it) }
    }

    fun next(): String = names.random()

    fun nextDiff(): String = names.filter { it != lastSelected }.random().also { lastSelected = it }

    fun next2(): Pair<String, String> = names.random().let { first ->
        Pair(first, names.filter { it != first }.random())
    }
}

fun main() {
    // Test CarEntity
    val car = CarEntity("Seat", "Leon")
    println("C'est une ${car.marque} ${car.model} de couleur ${car.couleur}")
    println(car)

    println("-------------------")

    // Test RandomName
    val randomName = RandomName()
    randomName.add("bobby")
    randomName.addAll("bobby", "Tobby", "Gustavo")
    repeat(10) {
        print(randomName.next() + " ")
    }
    println()
    repeat(10) {
        print(randomName.nextDiff() + " ")
    }
    println("\n ${randomName.next2()}")
}

