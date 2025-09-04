package com.isitech.bibliotheque.models;

/**
 * Classe représentant un professeur de la bibliothèque
 */
public class Professeur extends Utilisateur {
    private String departement;
    private boolean accesRessourcesSpeciales;

    /**
     * Constructeur pour un professeur
     * @param nom le nom du professeur
     * @param email l'email du professeur
     * @param departement le département d'enseignement
     */
    public Professeur(String nom, String email, String departement) {
        super(nom, email);
        this.departement = departement;
        this.accesRessourcesSpeciales = true;
        this.maxEmprunts = 10; // Plus d'emprunts pour les professeurs
    }

    @Override
    public int getDureeEmpruntMax() {
        return 30; // 1 mois pour les professeurs
    }

    @Override
    public String getTypeUtilisateur() {
        return "Professeur";
    }

    // Getters et Setters spécifiques

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public boolean hasAccesRessourcesSpeciales() {
        return accesRessourcesSpeciales;
    }

    public void setAccesRessourcesSpeciales(boolean accesRessourcesSpeciales) {
        this.accesRessourcesSpeciales = accesRessourcesSpeciales;
    }

    @Override
    public String toString() {
        return super.toString() + " - Département: " + departement;
    }
}