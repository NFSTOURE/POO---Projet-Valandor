package com.valandor.item;

import com.valandor.entity.Joueur;

/**
 * Représente une potion, cas particulier de Consommable.
 * Étend Consommable et spécialise l'effet selon le type de potion.
 *
 * Types disponibles :
 * - "soin"    : restaure des PV au joueur
 * - "force"   : augmente l'attaque du joueur
 * - "defense" : augmente la défense du joueur
 *
 * Relation POO : Potion "est un" Consommable (héritage)
 * La potion disparaît après utilisation (hérité de Consommable).
 */
public class Potion extends Consommable {

    // ── Attributs spécifiques à la potion ──────────────────
    private int    quantiteSoin; // quantité de soin ou de boost appliqué
    private String typePotion;   // "soin", "force" ou "defense"

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée une potion avec son type et sa puissance.
     * Le poids est fixé à 0.5 kg pour toutes les potions.
     *
     * @param nom          nom de la potion
     * @param quantiteSoin puissance de l'effet (PV soignés ou boost de stat)
     * @param typePotion   "soin", "force" ou "defense"
     */
    public Potion(String nom, int quantiteSoin, String typePotion) {
        super(nom,
              "Une potion de " + typePotion,
              0.5f,
              typePotion,
              quantiteSoin);
        this.quantiteSoin = quantiteSoin;
        this.typePotion   = typePotion;
    }

    // ── Application de l'effet ─────────────────────────────

    /**
     * Applique l'effet de la potion selon son type.
     * Surcharge la méthode de Consommable pour un effet spécialisé.
     *
     * - "soin"    : appelle joueur.soigner() pour restaurer des PV
     * - "force"   : augmente l'attaque du joueur de quantiteSoin
     * - "defense" : augmente la défense du joueur de quantiteSoin
     *
     * @param joueur le joueur qui boit la potion
     */
    @Override
    public void appliquerEffet(Joueur joueur) {
        switch (typePotion) {
            case "soin" -> {
                // Restaure des PV (limité au pvMax dans soigner())
                joueur.soigner(quantiteSoin);
                System.out.printf("  %s restaure %d PV !%n", nom, quantiteSoin);
            }
            case "force" -> {
                // Augmente l'attaque de façon permanente
                joueur.setAttaque(joueur.getAttaque() + quantiteSoin);
                System.out.printf("  %s augmente l'attaque de %d !%n",
                        nom, quantiteSoin);
            }
            case "defense" -> {
                // Augmente la défense de façon permanente
                joueur.setDefense(joueur.getDefense() + quantiteSoin);
                System.out.printf("  %s augmente la défense de %d !%n",
                        nom, quantiteSoin);
            }
            default -> System.out.println("  Effet inconnu : " + typePotion);
        }
    }

    // ── Getters ────────────────────────────────────────────

    /** @return la puissance de l'effet de la potion */
    public int    getQuantiteSoin() { return quantiteSoin; }

    /** @return le type de potion ("soin", "force", "defense") */
    public String getTypePotion()   { return typePotion; }
}