package com.isitech.bibliotheque.services;

import com.isitech.bibliotheque.models.Livre;
import com.isitech.bibliotheque.models.Utilisateur;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service principal pour la gestion de la bibliothèque
 */
public class BibliothequeService {
    private final Map<String, Livre> catalogueISBN; // Recherche rapide par ISBN
    private final Map<String, Set<Livre>> catalogueAuteur; // Index par auteur
    private final Set<Livre> livresDisponibles; // Cache des disponibles
    private final Map<String, Utilisateur> utilisateurs; // Gestion des utilisateurs
    private final String nomBibliotheque;

    /**
     * Constructeur du service bibliothèque
     * @param nomBibliotheque le nom de la bibliothèque
     */
    public BibliothequeService(String nomBibliotheque) {
        this.nomBibliotheque = nomBibliotheque;
        this.catalogueISBN = new HashMap<>();
        this.catalogueAuteur = new HashMap<>();
        this.livresDisponibles = new HashSet<>();
        this.utilisateurs = new HashMap<>();
    }

    // === GESTION DU CATALOGUE ===

    /**
     * Ajoute un livre au catalogue
     * @param livre le livre à ajouter
     * @throws Exception si le livre existe déjà
     */
    public void ajouterLivre(Livre livre) throws Exception {
        if (livre == null) {
            throw new Exception("Le livre ne peut pas être null");
        }
        
        if (catalogueISBN.containsKey(livre.getIsbn())) {
            throw new Exception("Un livre avec l'ISBN " + livre.getIsbn() + " existe déjà");
        }

        // Ajout dans les différents index
        catalogueISBN.put(livre.getIsbn(), livre);
        
        // Index par auteur
        catalogueAuteur.computeIfAbsent(livre.getAuteur().toLowerCase(), k -> new HashSet<>()).add(livre);
        
        if (livre.estDisponible()) {
            livresDisponibles.add(livre);
        }
        
        System.out.println("Livre ajouté: " + livre.getTitre());
    }

    /**
     * Supprime un livre du catalogue
     * @param isbn l'ISBN du livre à supprimer
     * @return true si supprimé, false si introuvable
     * @throws IllegalStateException si le livre est emprunté
     */
    public boolean supprimerLivre(String isbn) {
        Livre livre = catalogueISBN.remove(isbn);
        if (livre == null) {
            return false;
        }

        // Vérifier qu'il n'est pas emprunté
        if (!livre.estDisponible()) {
            catalogueISBN.put(isbn, livre); // Remettre
            throw new IllegalStateException("Impossible de supprimer un livre emprunté");
        }

        // Supprimer des index
        Set<Livre> livresAuteur = catalogueAuteur.get(livre.getAuteur().toLowerCase());
        if (livresAuteur != null) {
            livresAuteur.remove(livre);
            if (livresAuteur.isEmpty()) {
                catalogueAuteur.remove(livre.getAuteur().toLowerCase());
            }
        }

        livresDisponibles.remove(livre);
        System.out.println("Livre supprimé: " + livre.getTitre());
        return true;
    }

    // === RECHERCHES ===

    /**
     * Recherche un livre par ISBN
     * @param isbn l'ISBN à rechercher
     * @return le livre ou null
     */
    public Livre rechercherParIsbn(String isbn) {
        return catalogueISBN.get(isbn);
    }

    /**
     * Recherche des livres par auteur
     * @param auteur le nom de l'auteur
     * @return la liste des livres de cet auteur
     */
    public List<Livre> rechercherParAuteur(String auteur) {
        Set<Livre> livres = catalogueAuteur.get(auteur.toLowerCase());
        return livres != null ? 
            livres.stream().sorted().collect(Collectors.toList()) : 
            new ArrayList<>();
    }

    /**
     * Recherche textuelle dans les titres et auteurs
     * @param texte le texte à rechercher
     * @return la liste des livres correspondants
     */
    public List<Livre> rechercherTexte(String texte) {
        if (texte == null || texte.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String texteMinuscule = texte.toLowerCase();
        return catalogueISBN.values().stream()
            .filter(livre -> 
                livre.getTitre().toLowerCase().contains(texteMinuscule) ||
                livre.getAuteur().toLowerCase().contains(texteMinuscule))
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Obtient tous les livres du catalogue
     * @return la liste de tous les livres triée
     */
    public List<Livre> obtenirTousLesLivres() {
        return catalogueISBN.values().stream()
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Obtient les livres disponibles
     * @return la liste des livres disponibles
     */
    public List<Livre> obtenirLivresDisponibles() {
        return catalogueISBN.values().stream()
            .filter(Livre::estDisponible)
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Obtient les livres empruntés
     * @return la liste des livres empruntés
     */
    public List<Livre> obtenirLivresEmpruntes() {
        return catalogueISBN.values().stream()
            .filter(livre -> !livre.estDisponible())
            .sorted()
            .collect(Collectors.toList());
    }

    // === GESTION DES UTILISATEURS ===

    /**
     * Ajoute un utilisateur
     * @param utilisateur l'utilisateur à ajouter
     * @throws Exception si l'utilisateur existe déjà
     */
    public void ajouterUtilisateur(Utilisateur utilisateur) throws Exception {
        if (utilisateur == null) {
            throw new Exception("L'utilisateur ne peut pas être null");
        }

        if (utilisateurs.containsKey(utilisateur.getId())) {
            throw new Exception("Utilisateur avec l'ID " + utilisateur.getId() + " existe déjà");
        }

        // Vérifier unicité email
        boolean emailExiste = utilisateurs.values().stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(utilisateur.getEmail()));
        
        if (emailExiste) {
            throw new Exception("Un utilisateur avec l'email " + utilisateur.getEmail() + " existe déjà");
        }

        utilisateurs.put(utilisateur.getId(), utilisateur);
        System.out.println("Utilisateur ajouté: " + utilisateur.getNom() + " (" + utilisateur.getTypeUtilisateur() + ")");
    }

    /**
     * Recherche un utilisateur par ID
     * @param id l'ID de l'utilisateur
     * @return l'utilisateur ou null
     */
    public Utilisateur rechercherUtilisateur(String id) {
        return utilisateurs.get(id);
    }

    /**
     * Recherche des utilisateurs par nom
     * @param nom le nom à rechercher
     * @return la liste des utilisateurs correspondants
     */
    public List<Utilisateur> rechercherUtilisateurParNom(String nom) {
        return utilisateurs.values().stream()
            .filter(u -> u.getNom().toLowerCase().contains(nom.toLowerCase()))
            .sorted(Comparator.comparing(Utilisateur::getNom))
            .collect(Collectors.toList());
    }

    /**
     * Obtient tous les utilisateurs
     * @return la liste de tous les utilisateurs
     */
    public List<Utilisateur> obtenirTousUtilisateurs() {
        return new ArrayList<>(utilisateurs.values());
    }

    // === GESTION DES EMPRUNTS ===

    /**
     * Emprunte un livre
     * @param isbn l'ISBN du livre
     * @param idUtilisateur l'ID de l'utilisateur
     * @throws Exception si l'emprunt est impossible
     */
    public void emprunterLivre(String isbn, String idUtilisateur) throws Exception {
        Livre livre = rechercherParIsbn(isbn);
        if (livre == null) {
            throw new Exception("Livre avec ISBN " + isbn + " introuvable");
        }

        Utilisateur utilisateur = rechercherUtilisateur(idUtilisateur);
        if (utilisateur == null) {
            throw new Exception("Utilisateur avec ID " + idUtilisateur + " introuvable");
        }

        livre.emprunter(utilisateur);
        livresDisponibles.remove(livre);
        System.out.println("Emprunt effectué: " + livre.getTitre() + " par " + utilisateur.getNom());
    }

    /**
     * Retourne un livre
     * @param isbn l'ISBN du livre à retourner
     * @throws Exception si le retour est impossible
     */
    public void retournerLivre(String isbn) throws Exception {
        Livre livre = rechercherParIsbn(isbn);
        if (livre == null) {
            throw new Exception("Livre avec ISBN " + isbn + " introuvable");
        }

        if (livre.estDisponible()) {
            throw new Exception("Le livre n'est pas emprunté");
        }

        String nomEmprunteur = livre.getEmprunteur().getNom();
        livre.retourner();
        livresDisponibles.add(livre);
        
        System.out.println("Retour effectué: " + livre.getTitre() + " (était emprunté par " + nomEmprunteur + ")");
        
        if (livre.estEnRetard()) {
            System.out.println("ATTENTION: Retour en retard de " + livre.joursRetard() + " jours");
        }
    }

    // === STATISTIQUES ===

    /**
     * Affiche les statistiques de la bibliothèque
     */
    public void afficherStatistiques() {
        System.out.println("\n=== STATISTIQUES " + nomBibliotheque.toUpperCase() + " ===");
        System.out.println("Total livres: " + catalogueISBN.size());
        System.out.println("Livres disponibles: " + livresDisponibles.size());
        System.out.println("Livres empruntés: " + (catalogueISBN.size() - livresDisponibles.size()));
        System.out.println("Total utilisateurs: " + utilisateurs.size());
        
        // Statistiques par type d'utilisateur
        Map<String, Long> parType = utilisateurs.values().stream()
            .collect(Collectors.groupingBy(
                Utilisateur::getTypeUtilisateur,
                Collectors.counting()
            ));
        
        System.out.println("Utilisateurs par type:");
        parType.forEach((type, count) -> System.out.println("  " + type + ": " + count));
        System.out.println();
    }

    // Getters
    public String getNomBibliotheque() { 
        return nomBibliotheque; 
    }
    
    public int getTaileCatalogue() { 
        return catalogueISBN.size(); 
    }
    
    public int getNombreUtilisateurs() { 
        return utilisateurs.size(); 
    }
}