package com.isitech.bibliotheque.models;

import java.util.Objects;
import java.util.UUID;

/**
 * Classe abstraite représentant un utilisateur de la bibliothèque
 */
public abstract class Utilisateur {
    protected final String id;
    protected String nom;
    protected String email;
    protected int maxEmprunts;
    protected int empruntsActuels;

    /**
     * Constructeur pour un utilisateur
     * @param nom le nom de l'utilisateur
     * @param email l'email de l'utilisateur
     */
    protected Utilisateur(String nom, String email) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.nom = nom;
        this.email = email;
        this.empruntsActuels = 0;
    }

    // Méthodes abstraites à implémenter par les sous-classes
    
    /**
     * Obtient la durée maximale d'emprunt pour ce type d'utilisateur
     * @return la durée en jours
     */
    public abstract int getDureeEmpruntMax();
    
    /**
     * Obtient le type d'utilisateur sous forme de chaîne
     * @return le type d'utilisateur
     */
    public abstract String getTypeUtilisateur();

    // Méthodes concrètes communes

    /**
     * Vérifie si l'utilisateur peut effectuer un nouvel emprunt
     * @return true si possible, false sinon
     */
    public boolean peutEmprunter() {
        return empruntsActuels < maxEmprunts;
    }

    /**
     * Incrémente le nombre d'emprunts actuels
     * @throws IllegalStateException si le quota est déjà atteint
     */
    public void incrementerEmprunts() {
        if (empruntsActuels >= maxEmprunts) {
            throw new IllegalStateException("Quota d'emprunts atteint");
        }
        empruntsActuels++;
    }

    /**
     * Décrémente le nombre d'emprunts actuels
     */
    public void decrementerEmprunts() {
        if (empruntsActuels > 0) {
            empruntsActuels--;
        }
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMaxEmprunts() {
        return maxEmprunts;
    }

    public int getEmpruntsActuels() {
        return empruntsActuels;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utilisateur that = (Utilisateur) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s) - %d/%d emprunts",
                getTypeUtilisateur(), nom, email, empruntsActuels, maxEmprunts);
    }
}