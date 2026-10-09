package com.igensia.igensia_cours_mobile.exo

data class UserEntity(val name: String, val old: Int)

data class PersonEntity(val name: String, val note: Int)

fun main() {
    exo1()
    exo2()
    exo3()
}

fun exo1() {
    // 1. lower : Prend un texte et l’affiche en minuscule
    val lower: (String) -> Unit = { text -> println(text.lowercase()) }
    lower("Coucou")

    // 2. hour : Prenant un nombre de minutes et retournant le nombre d’heures équivalentes
    val hour: (Int) -> Double = { minutes -> minutes / 60.0 }
    println("150 minutes = ${hour(150)} heures")

    // 3. max : Prenant 2 entiers et retournant le plus grand
    val max: (Int, Int) -> Int = { a, b -> Math.max(a, b) }
    println("Max entre 10 et 25 = ${max(10, 25)}")

    // 4. reverse : Retourne le texte à l’envers
    val reverse: (String) -> String = { it.reversed() }
    println("Reverse de 'toto' = ${reverse("toto")}")

    // 5. minToMinHour : À partir d'un nombre de minutes (nullable), retourne Pair(heures, minutes) ou null
    var minToMinHour: ((Int?) -> Pair<Int, Int>?)? = { minutes ->
        minutes?.let { Pair(it / 60, it % 60) }
    }

    println("126 minutes -> ${minToMinHour?.invoke(126)}")
    println("null minutes -> ${minToMinHour?.invoke(null)}")

    // minToMinHour = null doit compiler et être appelable via safe call
    minToMinHour = null
    println("Après assignation à null -> ${minToMinHour?.invoke(126)}")
}

fun exo2() {
    val u1 = UserEntity("Alice", 25)
    val u2 = UserEntity("Bob", 25) // Même âge pour tester l'égalité
    val u3 = UserEntity("Charlie", 30)

    val compareUsersByName: (UserEntity, UserEntity) -> List<UserEntity> = { user1, user2 ->
        val comp = user1.name.lowercase().compareTo(user2.name.lowercase())
        when {
            comp < 0 -> listOf(user1)
            comp > 0 -> listOf(user2)
            else -> listOf(user1, user2)
        }
    }

    val compareUsersByOld: (UserEntity, UserEntity) -> List<UserEntity> = { user1, user2 ->
        when {
            user1.old > user2.old -> listOf(user1)
            user2.old > user1.old -> listOf(user2)
            else -> listOf(user1, user2)
        }
    }

    println("Comparaison par nom : ${compareUsersByName(u1, u2).map { it.name }}")
    println("Comparaison par age (différents) : ${compareUsersByOld(u1, u3).map{it.name}}")
    println("Comparaison par âge (égalité) : ${compareUsersByOld(u1, u2).map { it.name }}")
}

fun exo3() {
    val list = mutableListOf(
        PersonEntity("Toto", 8),
        PersonEntity("Toto", 12),
        PersonEntity("toto", 5),
        PersonEntity("Bob", 14),
        PersonEntity("Bob", 14),
        PersonEntity("Alice", 10),
        PersonEntity("Charlie", 5),
        PersonEntity("David", 3)
    )

    // Variable isToto pour éviter les répétitions
    val isToto: (PersonEntity) -> Boolean = { it.name.equals("Toto", ignoreCase = true) }

    // 1. Afficher la sous liste de personne ayant 10 et + (filter)
    println("--- Notes >= 10 ---")
    println(list.filter { it.note >= 10 })

    // 2. Afficher combien il y a de Toto dans la classe ? (count)
    println("Nombre de Toto : ${list.count(isToto)}")

    // 3. Afficher combien de Toto ayant la moyenne (10 et +)
    println("Nombre de Toto avec la moyenne : ${list.count { isToto(it) && it.note >= 10 }}")

    // 4. Afficher combien de Toto ont plus que la moyenne de la classe (map + average)
    val averageNote = list.map { it.note }.average()
    println("Moyenne de la classe : $averageNote")
    println("Nombre de Toto au-dessus de la moyenne : ${list.count { isToto(it) && it.note > averageNote }}")

    // 5. Afficher les noms sans doublon (distinct) par ordre alphabétique
    println("Noms uniques triés : ${list.map { it.name }.distinct().sorted()}")

    // 6. Ajouter un point à ceux n’ayant pas la moyenne (<10)
    list.replaceAll { if (it.note < 10) it.copy(note = it.note + 1) else it }

    // 7. Ajouter un point à tous les Toto
    list.replaceAll { if (isToto(it)) it.copy(note = it.note + 1) else it }

    // 8. Retirer de la liste (removeIf) ceux ayant la note la plus petite (Il peut y en avoir plusieurs)
    val minNote = list.minOfOrNull { it.note }
    if (minNote != null) {
        list.removeIf { it.note == minNote }
    }

    // 9. Afficher les noms de ceux ayant la moyenne(10et+) par ordre alphabétique
    println("Noms ayant la moyenne (triés) : ${list.filter { it.note >= 10 }.map { it.name }.sorted()}")

    // 12. Dupliquer la liste ainsi que tous les utilisateurs (nouvelle instance) qu'elle contient (map + copy)
    val duplicatedList = list.map { it.copy() }
    println("Liste dupliquée : $duplicatedList")

    // Pour les plus rapides : Afficher par notes croissantes, les personnes ayant eu cette note (.groupBy {})
    println("--- Groupement par notes croissantes ---")
    list.groupBy { it.note }
        .toSortedMap()
        .forEach { (note, persons) ->
            println("$note : ${persons.joinToString(", ") { it.name }}")
        }
}

