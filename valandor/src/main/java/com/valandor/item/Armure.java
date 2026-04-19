package com.valandor.item;

import com.valandor.entity.Joueur;

/**
 * Représente une pièce d'armure équipable par le joueur.
 * Étend Objet et ajoute :
 * - Un bonus de défense appliqué au joueur quand équipée
 * - Un emplacement d'équipement ("armure", "casque", "bottes")
 *
 * Relation POO : Armure "est un" Objet (héritage)
 * Quand équipée : défense du joueur += bonusDefense
 * Quand déséquipée : défense du joueur -= bonusDefense
 */
public class Armure extends Objet {

    // ── Attributs spécifiques à l'armure ───────────────────
    private int    bonusDefense; // points de défense ajoutés au joueur
    private String emplacement;  // "armure", "casque" ou "bottes"

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée une pièce d'armure avec ses caractéristiques.
     *
     * @param nom          nom de l'armure
     * @param description  description courte
     * @param poids        poids en kg
     * @param bonusDefense points de défense ajoutés quand équipée
     * @param emplacement  emplacement d'équipement ("armure", "casque")
     */
    public Armure(String nom, String description, float poids,
                  int bonusDefense, String emplacement) {
        super(nom, description, poids, bonusDefense);
        this.bonusDefense = bonusDefense;
        this.emplacement  = emplacement;
    }

    // ── Gestion des bonus ──────────────────────────────────

    /**
     * Applique le bonus de défense au joueur.
     * Appelée quand l'armure est équipée.
     *
     * @param joueur le joueur qui reçoit le bonus
     */
    public void appliquerBonus(Joueur joueur) {
        joueur.setDefense(joueur.getDefense() + bonusDefense);
        System.out.println("  +" + bonusDefense + " défense appliqué.");
    }

    /**
     * Retire le bonus de défense du joueur.
     * Appelée quand l'armure est déséquipée.
     *
     * @param joueur le joueur qui perd le bonus
     */
    public void retirerBonus(Joueur joueur) {
        joueur.setDefense(joueur.getDefense() - bonusDefense);
        System.out.println("  -" + bonusDefense + " défense retiré.");
    }

    // ── Utilisation ────────────────────────────────────────

    /**
     * Équipe cette armure sur le joueur dans son emplacement.
     * Applique automatiquement le bonus de défense.
     *
     * @param cible le joueur qui équipe l'armure
     */
    @Override
    public void utiliser(Object cible) {
        if (cible instanceof Joueur joueur) {
            joueur.equiper(this, emplacement);
            appliquerBonus(joueur);
        }
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Retourne une description détaillée de l'armure.
     * Format : Nom | +X DEF | Emplacement | Poids kg
     */
    @Override
    public String toString() {
        return String.format("%s | +%d DEF | %s | %.1f kg",
                nom, bonusDefense, emplacement, poids);
    }

    // ── Getters ────────────────────────────────────────────

    /** @return le bonus de défense de l'armure */
    public int    getBonusDefense() { return bonusDefense; }

    /** @return l'emplacement d'équipement ("armure", "casque") */
    public String getEmplacement()  { return emplacement; }
}