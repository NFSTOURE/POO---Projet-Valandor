package com.valandor.item;

/**
 * Classe abstraite représentant tout objet du monde de Valandor.
 * C'est la classe mère de la hiérarchie des objets.
 * Elle factorise les attributs communs (nom, description, poids, valeur)
 * et force chaque sous-classe à implémenter la méthode utiliser().
 *
 * Sous-classes : Arme, Armure, Consommable → Potion
 *
 * Relation POO : Objet est utilisé par composition dans Inventaire
 * et dans les emplacements d'équipement du Joueur.
 */
public abstract class Objet {

    // ── Attributs communs à tous les objets ────────────────
    protected String nom;         // nom affiché dans l'inventaire
    protected String description; // description affichée à l'examen
    protected float  poids;       // poids en kg (limite l'inventaire)
    protected int    valeur;      // valeur en or / bonus de stats

    // ── Constructeur ───────────────────────────────────────

    /**
     * Initialise un objet avec ses propriétés de base.
     *
     * @param nom         nom de l'objet
     * @param description description courte
     * @param poids       poids en kg
     * @param valeur      valeur en or ou bonus de stats
     */
    public Objet(String nom, String description, float poids, int valeur) {
        this.nom         = nom;
        this.description = description;
        this.poids       = poids;
        this.valeur      = valeur;
    }

    // ── Méthode abstraite ──────────────────────────────────

    /**
     * Définit l'effet de l'objet quand il est utilisé.
     * Chaque sous-classe implémente son propre comportement :
     * - Arme      : s'équipe dans l'emplacement "arme"
     * - Armure    : s'équipe et applique un bonus de défense
     * - Potion    : soigne ou booste les stats du joueur
     * - Consommable : applique un effet et disparaît
     *
     * @param cible l'entité qui utilise l'objet (généralement le Joueur)
     */
    public abstract void utiliser(Object cible);

    // ── Méthodes concrètes ─────────────────────────────────

    /**
     * Retourne une représentation textuelle de l'objet.
     * Format : Nom (poids kg) — description
     */
    @Override
    public String toString() {
        return String.format("%s (%.1f kg) — %s", nom, poids, description);
    }

    // ── Getters ────────────────────────────────────────────

    /** @return le nom de l'objet */
    public String getNom()          { return nom; }

    /** @return la description de l'objet */
    public String getDescription()  { return description; }

    /** @return le poids de l'objet en kg */
    public float  getPoids()        { return poids; }

    /** @return la valeur ou le bonus de stats de l'objet */
    public int    getValeur()       { return valeur; }
}