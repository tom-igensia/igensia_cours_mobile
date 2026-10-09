package com.igensia.igensia_cours_mobile.exo;

public class MainJava {
    public static void main(String[] args) {
        // Appel de la méthode boulangerie définie en Kotlin (via la classe générée ExokotlinKt)
        double total = ExokotlinKt.boulangerie(2, 0, 0);
        System.out.println("Total boulangerie depuis Java : " + total);
    }
}

