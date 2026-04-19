package com.valandor.entity;

/**
 * Classe abstraite représentant toute entité vivante du jeu.
 * C'est la classe mère de la hiérarchie des entités.
 * Elle factorise les attributs communs (PV, stats de combat)
 * et les comportements partagés (attaquer, soigner, subir des dégâts).
 *
 * Sous-classes : Joueur, Ennemi, PNJ
 */
public abstract class EntiteVivante {

    // ── Attributs communs à toutes les entités ─────────────
    protected String nom;         // nom affiché en combat et en description
    protected int    pvMax;       // points de vie maximum
    protected int    pvCourants;  // points de vie actuels (0 = mort)
    protected int    attaque;     // puissance d'attaque de base
    protected int    defense;     // absorption des dégâts reçus

    // ── Constructeur ───────────────────────────────────────

    /**
     * Initialise une entité vivante avec ses stats de base.
     * Les PV courants sont initialisés au maximum (entité en pleine santé).
     *
     * @param nom     nom de l'entité
     * @param pvMax   points de vie maximum
     * @param attaque puissance d'attaque
     * @param defense capacité d'absorption des dégâts
     */
    public EntiteVivante(String nom, int pvMax, int attaque, int defense) {
        this.nom        = nom;
        this.pvMax      = pvMax;
        this.pvCourants = pvMax; // commence avec tous ses PV
        this.attaque    = attaque;
        this.defense    = defense;
    }

    // ── Méthodes concrètes partagées ───────────────────────

    /**
     * Applique des dégâts à cette entité en tenant compte de sa défense.
     * Les dégâts réels = dégâts reçus - défense (minimum 0).
     * Les PV ne peuvent pas descendre en dessous de 0.
     *
     * @param degats dégâts bruts infligés avant absorption
     */
    public void subirDegats(int degats) {
        // La défense absorbe une partie des dégâts
        int degatsReels = Math.max(0, degats - this.defense);
        this.pvCourants = Math.max(0, this.pvCourants - degatsReels);
        System.out.printf("  %s subit %d dégâts (%d absorbés) → %d/%d PV%n",
                nom, degatsReels, degats - degatsReels, pvCourants, pvMax);
    }

    /**
     * Soigne cette entité d'une certaine quantité de PV.
     * Les PV ne peuvent pas dépasser le maximum.
     *
     * @param quantite points de vie à restaurer
     */
    public void soigner(int quantite) {
        int anciensPV   = this.pvCourants;
        // Math.min garantit qu'on ne dépasse pas pvMax
        this.pvCourants = Math.min(pvMax, pvCourants + quantite);
        System.out.printf("  %s récupère %d PV → %d/%d PV%n",
                nom, pvCourants - anciensPV, pvCourants, pvMax);
    }

    /**
     * Vérifie si cette entité est encore en vie.
     * @return true si les PV courants sont supérieurs à 0
     */
    public boolean estVivant() {
        return this.pvCourants > 0;
    }

    /**
     * Attaque une autre entité vivante avec la puissance d'attaque de base.
     * La cible applique ensuite sa défense pour réduire les dégâts.
     *
     * @param cible l'entité qui reçoit l'attaque
     */
    public void attaquer(EntiteVivante cible) {
        System.out.printf("  %s attaque %s !%n", this.nom, cible.getNom());
        cible.subirDegats(this.attaque);
    }

    /**
     * Retourne un résumé des stats de l'entité sous forme de chaîne.
     * Format : [Nom] PV: x/y | ATT: z | DEF: w
     */
    public String getStats() {
        return String.format("[%s] PV: %d/%d | ATT: %d | DEF: %d",
                nom, pvCourants, pvMax, attaque, defense);
    }

    // ── Méthode abstraite ──────────────────────────────────

    /**
     * Définit le comportement de l'entité lors de son tour.
     * Chaque sous-classe implémente sa propre logique :
     * - Joueur : attend une commande clavier
     * - Ennemi : attaque automatiquement
     * - PNJ    : ne fait rien (réagit uniquement quand on lui parle)
     *
     * @param contexte le contexte de jeu (accès au joueur, au lieu, etc.)
     */
    public abstract void agir(Object contexte);

    // ── Getters & Setters ──────────────────────────────────

    /** @return le nom de l'entité */
    public String getNom()      { return nom; }

    /** @return les points de vie maximum */
    public int getPvMax()       { return pvMax; }

    /** @return les points de vie actuels */
    public int getPvCourants()  { return pvCourants; }

    /** @return la puissance d'attaque */
    public int getAttaque()     { return attaque; }

    /** @return la valeur de défense */
    public int getDefense()     { return defense; }

    /**
     * Modifie la puissance d'attaque (utilisé par les bonus d'équipement).
     */
    public void setAttaque(int attaque) { this.attaque = attaque; }

    /**
     * Modifie la valeur de défense (utilisé par les bonus d'équipement).
     */
    public void setDefense(int defense) { this.defense = defense; }

    /**
     * Modifie les PV courants en les contraignant entre 0 et pvMax.
     * Utilisé lors du chargement d'une sauvegarde.
     */
    public void setPvCourants(int pv) {
        this.pvCourants = Math.max(0, Math.min(pvMax, pv));
    }
}