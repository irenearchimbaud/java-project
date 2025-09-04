package com.isitech.bibliotheque.models;

import com.isitech.bibliotheque.interfaces.Empruntable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe représentant un livre de la bibliothèque
 */
public class Livre implements Empruntable, Comparable<Livre> {
    private final String isbn;
    private String titre;
    private String auteur;
    private int nbPages;
    private String editeur;
    private LocalDate datePublication;
    
    // État d'emprunt
    private boolean disponible;
    private Utilisateur emprunteur;
    private LocalDate dateEmprunt;

    /**
     * Constructeur minimal pour un livre
     * @param isbn l'ISBN du livre
     * @param titre le titre du livre
     * @param auteur l'auteur du livre
     */
    public Livre(String isbn, String titre, String auteur) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.disponible = true;
        this.nbPages = 0;
    }

    /**
     * Constructeur complet pour un livre
     */
    public Livre(String isbn, String titre, String auteur, int nbPages, String editeur, LocalDate datePublication) {
        this(isbn, titre, auteur);
        this.nbPages = nbPages;
        this.editeur = editeur;
        this.datePublication = datePublication;
    }

    // Implémentation de l'interface Empruntable

    @Override
    public boolean estDisponible() {
        return disponible && emprunteur == null;
    }

    @Override
    public void emprunter(Utilisateur utilisateur) throws Exception {
        if (!estDisponible()) {
            throw new Exception("Livre déjà emprunté");
        }
        if (!utilisateur.peutEmprunter()) {
            throw new Exception("Quota d'emprunts dépassé pour " + utilisateur.getNom());
        }

        this.emprunteur = utilisateur;
        this.dateEmprunt = LocalDate.now();
        this.disponible = false;
        utilisateur.incrementerEmprunts();
    }

    @Override
    public void retourner() {
        if (estDisponible()) {
            throw new IllegalStateException("Livre déjà disponible");
        }

        emprunteur.decrementerEmprunts();
        this.emprunteur = null;
        this.dateEmprunt = null;
        this.disponible = true;
    }

    @Override
    public Utilisateur getEmprunteur() {
        return emprunteur;
    }

    @Override
    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    @Override
    public LocalDate getDateRetourPrevue() {
        if (dateEmprunt == null || emprunteur == null) {
            return null;
        }
        return dateEmprunt.plusDays(emprunteur.getDureeEmpruntMax());
    }

    // Implémentation de Comparable pour le tri

    @Override
    public int compareTo(Livre autre) {
        // Tri par titre, puis par auteur
        int comparaisonTitre = this.titre.compareToIgnoreCase(autre.titre);
        if (comparaisonTitre != 0) {
            return comparaisonTitre;
        }
        return this.auteur.compareToIgnoreCase(autre.auteur);
    }

    // Méthodes utilitaires

    /**
     * Vérifie si le livre est en retard
     * @return true si en retard, false sinon
     */
    public boolean estEnRetard() {
        LocalDate dateRetourPrevue = getDateRetourPrevue();
        return dateRetourPrevue != null && LocalDate.now().isAfter(dateRetourPrevue);
    }

    /**
     * Calcule le nombre de jours de retard
     * @return le nombre de jours de retard (0 si pas en retard)
     */
    public long joursRetard() {
        if (!estEnRetard()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(getDateRetourPrevue(), LocalDate.now());
    }

    // Getters et Setters

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livre livre = (Livre) obj;
        return Objects.equals(isbn, livre.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        String statut = disponible ? "Disponible" : "Emprunté par " + emprunteur.getNom();
        return String.format("'%s' par %s (ISBN: %s) - %s", titre, auteur, isbn, statut);
    }
}