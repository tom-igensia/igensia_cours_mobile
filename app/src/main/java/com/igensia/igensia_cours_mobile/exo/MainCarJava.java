package com.igensia.igensia_cours_mobile.exo;

public class MainCarJava {
    public static void main(String[] args) {
        // Création d'une Seat Leon depuis Java
        CarEntity car = new CarEntity("Seat", "Leon");
        
        System.out.println("C'est une " + car.getMarque() + " " + car.getModel() + " de couleur " + car.getCouleur());
        System.out.println(car);
    }
}
