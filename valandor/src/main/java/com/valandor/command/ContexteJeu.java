package com.valandor.command;

import com.valandor.entity.Joueur;
import com.valandor.world.Lieu;
import java.util.HashMap;
import java.util.Map;

/**
 * Contient l'etat global de la partie en cours.
 * Passe en parametre a chaque commande pour leur donner acces
 * au joueur, au lieu courant et au monde entier.
 *
 * Relation POO : ContexteJeu est utilise par composition
 * dans toutes les classes Command (patron Command).
 * C'est le lien entre les commandes et l'etat du jeu.
 */
public class ContexteJeu {

    // ── Attributs ──────────────────────────────────────────
    private Joueur            joueur;      // le personnage du joueur
    private Lieu              lieuCourant; // lieu ou se trouve le joueur
    private Map<String, Lieu> monde;       // tous les lieux du jeu
    private boolean           enCours;     // false = partie terminee

    // ── Constructeur ───────────────────────────────────────

    /**
     * Cree un nouveau contexte de jeu avec le joueur et son lieu de depart.
     *
     * @param joueur     le personnage du joueur
     * @param lieuDepart le lieu initial au demarrage de la partie
     */
    public ContexteJeu(Joueur joueur, Lieu lieuDepart) {
        this.joueur      = joueur;
        this.lieuCourant = lieuDepart;
        this.monde       = new HashMap<>();
        this.enCours     = true;
    }

    // ── Gestion du monde ───────────────────────────────────

    /**
     * Enregistre un lieu dans le monde avec un identifiant unique.
     * Permet de retrouver un lieu par son id lors du chargement.
     *
     * @param id   identifiant unique du lieu (ex: "village", "foret")
     * @param lieu le lieu a enregistrer
     */
    public void ajouterLieu(String id, Lieu lieu) {
        monde.put(id, lieu);
    }

    /**
     * Retourne un lieu du monde par son identifiant.
     *
     * @param id identifiant du lieu recherche
     * @return le lieu correspondant ou null si introuvable
     */
    public Lieu getLieu(String id) {
        return monde.get(id);
    }

    // ── Gestion de la partie ───────────────────────────────

    /**
     * Termine la partie en cours.
     * La boucle principale dans Main.java s'arrete
     * quand enCours passe a false.
     */
    public void terminer() {
        this.enCours = false;
    }

    // ── Getters & Setters ──────────────────────────────────

    /** @return le joueur de la partie */
    public Joueur  getJoueur()      { return joueur; }

    /** @return le lieu ou se trouve actuellement le joueur */
    public Lieu    getLieuCourant() { return lieuCourant; }

    /**
     * Deplace le joueur vers un nouveau lieu.
     * Appele par CommandeAller apres verification de la sortie.
     */
    public void    setLieuCourant(Lieu lieu) { this.lieuCourant = lieu; }

    /** @return true si la partie est en cours */
    public boolean isEnCours()      { return enCours; }
}