package com.valandor.entity;

import com.valandor.world.Quete;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un Personnage Non-Joueur (PNJ) du monde de Valandor.
 * Étend EntiteVivante et ajoute :
 * - Un système de dialogues par sujet (Map sujet → réponse)
 * - Une liste de quêtes proposables au joueur
 *
 * Relation POO : PNJ "est une" EntiteVivante (héritage)
 * Différence avec Ennemi : le PNJ n'est pas combattable
 * et réagit uniquement quand le joueur lui parle.
 */
public class PNJ extends EntiteVivante {

    // ── Attributs spécifiques au PNJ ───────────────────────
    private Map<String, String> dialogues;      // sujet → réponse
    private String              dialogueDefaut; // réponse si sujet inconnu
    private List<Quete>         quetesDisponibles; // quêtes proposables

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un PNJ avec un nom et un dialogue par défaut.
     * Les PNJ ont 50 PV, 0 ATT et 0 DEF car ils ne combattent pas.
     *
     * @param nom            nom du PNJ affiché au joueur
     * @param dialogueDefaut réponse affichée si le sujet est inconnu
     */
    public PNJ(String nom, String dialogueDefaut) {
        super(nom, 50, 0, 0);
        this.dialogues          = new HashMap<>();
        this.dialogueDefaut     = dialogueDefaut;
        this.quetesDisponibles  = new ArrayList<>();
    }

    // ── Système de dialogues ───────────────────────────────

    /**
     * Enregistre une réponse pour un sujet de conversation donné.
     * Le sujet est stocké en minuscules pour une recherche insensible à la casse.
     *
     * @param sujet   mot-clé de la conversation (ex: "quete", "bonjour")
     * @param reponse texte affiché quand le joueur aborde ce sujet
     */
    public void ajouterDialogue(String sujet, String reponse) {
        dialogues.put(sujet.toLowerCase(), reponse);
    }

    /**
     * Déclenche un dialogue du PNJ sur un sujet donné.
     * Si le sujet n'est pas connu, affiche le dialogue par défaut.
     *
     * @param sujet le sujet abordé par le joueur
     * @return la réponse du PNJ
     */
    public String parler(String sujet) {
        // Cherche la réponse au sujet, ou utilise le dialogue par défaut
        String reponse = dialogues.getOrDefault(
                sujet.toLowerCase(), dialogueDefaut);
        System.out.println("\n  " + nom + " : \"" + reponse + "\"");
        return reponse;
    }

    /**
     * Déclenche le dialogue de salutation ("bonjour").
     */
    public void saluer() {
        parler("bonjour");
    }

    // ── Système de quêtes ──────────────────────────────────

    /**
     * Ajoute une quête proposable par ce PNJ.
     *
     * @param quete la quête à ajouter au PNJ
     */
    public void ajouterQuete(Quete quete) {
        quetesDisponibles.add(quete);
    }

    /**
     * @return la liste des quêtes que ce PNJ peut proposer
     */
    public List<Quete> getQuetesDisponibles() {
        return quetesDisponibles;
    }

    // ── Implémentation de la méthode abstraite ─────────────

    /**
     * Le PNJ n'effectue aucune action automatique.
     * Il réagit uniquement quand le joueur lui parle (via CommandeParler).
     */
    @Override
    public void agir(Object contexte) {
        // Le PNJ est passif — aucune action automatique
    }

    // ── Getters & Setters ──────────────────────────────────

    /** @return la map des dialogues (sujet → réponse) */
    public Map<String, String> getDialogues()     { return dialogues; }

    /** @return le dialogue affiché quand le sujet est inconnu */
    public String getDialogueDefaut()             { return dialogueDefaut; }

    /** Modifie le dialogue par défaut */
    public void setDialogueDefaut(String msg)     { this.dialogueDefaut = msg; }
}