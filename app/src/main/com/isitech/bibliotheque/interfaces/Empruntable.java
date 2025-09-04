package com.isitech.bibliotheque.interfaces;

import java.time.LocalDate;
import com.isitech.bibliotheque.models.Utilisateur;

/**
 * Interface définissant le contrat pour les objets empruntables
 */
public interface Empruntable {
    
    /**
     * Vérifie si l'objet peut être emprunté
     * @return true si disponible, false sinon
     */
    boolean estDisponible();
    
    /**
     * Emprunte l'objet à un utilisateur
     * @param utilisateur l'utilisateur qui emprunte
     * @throws Exception si l'emprunt est impossible
     */
    void emprunter(Utilisateur utilisateur) throws Exception;
    
    /**
     * Retourne l'objet emprunté
     * @throws IllegalStateException si l'objet n'est pas emprunté
     */
    void retourner();
    
    /**
     * Obtient l'utilisateur actuel (null si disponible)
     * @return l'utilisateur qui a emprunté ou null
     */
    Utilisateur getEmprunteur();
    
    /**
     * Obtient la date d'emprunt (null si disponible)
     * @return la date d'emprunt ou null
     */
    LocalDate getDateEmprunt();
    
    /**
     * Calcule la date de retour prévue
     * @return la date de retour prévue ou null
     */
    LocalDate getDateRetourPrevue();
}