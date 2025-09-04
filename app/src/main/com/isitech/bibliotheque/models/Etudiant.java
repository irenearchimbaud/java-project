package com.isitech.bibliotheque.models;

/**
 * Classe représentant un étudiant de la bibliothèque
 */
public class Etudiant extends Utilisateur {
    private String numeroEtudiant;
    private int niveau; // 1=L1, 2=L2, 3=L3, 4=M1, 5=M2
    private String filiere;

    /**
     * Constructeur pour un étudiant
     * @param nom le nom de l'étudiant
     * @param email l'email de l'étudiant
     * @param numeroEtudiant le numéro d'étudiant
     * @param niveau le niveau d'étude (1-5)
     * @param filiere la filière d'étude
     */
    public Etudiant(String nom, String email, String numeroEtudiant, int niveau, String filiere) {
        super(nom, email);
        this.numeroEtudiant = numeroEtudiant;
        this.niveau = niveau;
        this.filiere = filiere;
        // Plus d'emprunts pour les étudiants de Master
        this.maxEmprunts = niveau <= 3 ? 3 : 5;
    }

    @Override
    public int getDureeEmpruntMax() {
        return 15; // 15 jours pour les étudiants
    }

    @Override
    public String getTypeUtilisateur() {
        return "Étudiant " + getNiveauLibelle();
    }

    /**
     * Convertit le niveau numérique en libellé
     * @return le libellé du niveau (L1, L2, L3, M1, M2)
     */
    private String getNiveauLibelle() {
        return switch (niveau) {
            case 1 -> "L1";
            case 2 -> "L2";
            case 3 -> "L3";
            case 4 -> "M1";
            case 5 -> "M2";
            default -> "N/A";
        };
    }

    // Getters spécifiques à l'étudiant

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }
}