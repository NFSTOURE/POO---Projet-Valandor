package com.valandor.world;

import com.valandor.entity.Joueur;
import com.valandor.item.Objet;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une quête que le joueur peut accepter et accomplir.
 * Une quête définit :
 * - Un objectif à remplir (tuer X ennemis, visiter un lieu, etc.)
 * - Des récompenses (XP et objets) données à la complétion
 *
 * Cycle de vie : DISPONIBLE → ACCEPTÉE → EN COURS → TERMINÉE
 * La progression est suivie par quantiteCourante / quantiteRequise.
 */
public class Quete {

    // ── Attributs ──────────────────────────────────────────
    private String      titre;             // nom de la quête
    private String      description;       // description affichée au joueur
    private int         recompenseXP;      // XP donnée à la complétion
    private List<Objet> recompensesObjets; // objets donnés à la complétion
    private boolean     estTerminee;       // true si la quête est complétée
    private boolean     estAcceptee;       // true si le joueur a accepté
    private String      objectif;          // description textuelle de l'objectif
    private String      typeObjectif;      // "tuer", "collecter", "visiter"
    private String      cibleObjectif;     // nom de la cible (ennemi, objet, lieu)
    private int         quantiteRequise;   // quantité nécessaire pour compléter
    private int         quantiteCourante;  // progression actuelle

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée une nouvelle quête avec ses paramètres de complétion.
     *
     * @param titre           nom de la quête
     * @param description     description affichée au joueur
     * @param recompenseXP    XP donnée à la complétion
     * @param objectif        description textuelle de l'objectif
     * @param typeObjectif    "tuer", "collecter" ou "visiter"
     * @param cibleObjectif   nom de la cible à atteindre
     * @param quantiteRequise nombre d'actions nécessaires
     */
    public Quete(String titre, String description, int recompenseXP,
                 String objectif, String typeObjectif,
                 String cibleObjectif, int quantiteRequise) {
        this.titre             = titre;
        this.description       = description;
        this.recompenseXP      = recompenseXP;
        this.objectif          = objectif;
        this.typeObjectif      = typeObjectif;
        this.cibleObjectif     = cibleObjectif;
        this.quantiteRequise   = quantiteRequise;
        this.quantiteCourante  = 0;
        this.recompensesObjets = new ArrayList<>();
        this.estTerminee       = false;
        this.estAcceptee       = false;
    }

    // ── Gestion de la quête ────────────────────────────────

    /**
     * Marque la quête comme acceptée par le joueur.
     * Affiche le titre et l'objectif pour informer le joueur.
     *
     * @param joueur le joueur qui accepte la quête
     */
    public void accepter(Joueur joueur) {
        if (estAcceptee) {
            System.out.println("  Quête déjà acceptée : " + titre);
            return;
        }
        estAcceptee = true;
        System.out.println("  Quête acceptée : " + titre);
        System.out.println("  Objectif : " + objectif);
    }

    /**
     * Incrémente la progression de la quête quand la cible est atteinte.
     * Vérifie que le nom correspond à la cible de la quête.
     *
     * @param cible nom de l'action effectuée (ex: nom de l'ennemi tué)
     */
    public void progresser(String cible) {
        if (!estAcceptee || estTerminee) return;
        if (cible.equalsIgnoreCase(cibleObjectif)) {
            quantiteCourante++;
            System.out.printf("  Quête [%s] : %d/%d%n",
                    titre, quantiteCourante, quantiteRequise);
        }
    }

    /**
     * Vérifie si les conditions de complétion sont remplies
     * et termine la quête si c'est le cas.
     *
     * @param joueur le joueur dont on vérifie la progression
     */
    public void verifierEtTerminer(Joueur joueur) {
        if (quantiteCourante >= quantiteRequise) {
            terminer(joueur);
        }
    }

    /**
     * Termine la quête et distribue les récompenses au joueur.
     * Donne l'XP et les objets de récompense.
     *
     * @param joueur le joueur qui reçoit les récompenses
     */
    public void terminer(Joueur joueur) {
        if (estTerminee) {
            System.out.println("  Quête déjà terminée : " + titre);
            return;
        }
        estTerminee = true;
        System.out.println("\n  ★ Quête accomplie : " + titre + " ★");

        // Distribution des récompenses
        joueur.gagnerXP(recompenseXP);
        for (Objet objet : recompensesObjets) {
            joueur.getInventaire().ajouterObjet(objet);
            System.out.println("  Récompense : " + objet.getNom());
        }
    }

    /**
     * Ajoute un objet à la liste des récompenses de la quête.
     *
     * @param objet objet donné au joueur à la complétion
     */
    public void ajouterRecompense(Objet objet) {
        recompensesObjets.add(objet);
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Affiche les informations de la quête dans le journal.
     * Indique le statut, la description, la progression et la récompense.
     */
    public void afficher() {
        String statut = estTerminee   ? "[TERMINÉE]"   :
                        estAcceptee   ? "[EN COURS]"   :
                                        "[DISPONIBLE]";
        System.out.printf("  %s %s%n", statut, titre);
        System.out.println("    " + description);
        System.out.printf("    Objectif   : %s (%d/%d)%n",
                objectif, quantiteCourante, quantiteRequise);
        System.out.println("    Récompense : " + recompenseXP + " XP");
    }

    // ── Getters ────────────────────────────────────────────

    /** @return le titre de la quête */
    public String  getTitre()         { return titre; }

    /** @return la description de la quête */
    public String  getDescription()   { return description; }

    /** @return l'XP de récompense */
    public int     getRecompenseXP()  { return recompenseXP; }

    /** @return true si la quête est terminée */
    public boolean estTerminee()      { return estTerminee; }

    /** @return true si la quête est acceptée */
    public boolean estAcceptee()      { return estAcceptee; }

    /** @return la description textuelle de l'objectif */
    public String  getObjectif()      { return objectif; }

    /** @return la progression actuelle */
    public int     getQuantiteCourante() { return quantiteCourante; }

    /** @return la quantité requise pour compléter */
    public int     getQuantiteRequise()  { return quantiteRequise; }
}