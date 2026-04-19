package com.valandor.item;

import com.valandor.entity.Joueur;

/**
 * Représente un objet consommable à usage unique.
 * Étend Objet et ajoute :
 * - Un effet nommé (ex: "soin", "force", "defense")
 * - Une valeur d'effet (quantité appliquée)
 * L'objet disparaît de l'inventaire après utilisation.
 *
 * Relation POO : Consommable "est un" Objet (héritage)
 * Sous-classe : Potion (cas particulier de consommable)
 */
public class Consommable extends Objet {

    // ── Attributs spécifiques au consommable ───────────────
    protected String effet;       // type d'effet ("soin", "force", "defense")
    protected int    valeurEffet; // intensité de l'effet

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un consommable avec son effet.
     *
     * @param nom         nom de l'objet
     * @param description description courte
     * @param poids       poids en kg
     * @param effet       type d'effet appliqué
     * @param valeurEffet intensité de l'effet
     */
    public Consommable(String nom, String description, float poids,
                       String effet, int valeurEffet) {
        super(nom, description, poids, 0);
        this.effet       = effet;
        this.valeurEffet = valeurEffet;
    }

    // ── Utilisation ────────────────────────────────────────

    /**
     * Utilise le consommable sur le joueur.
     * Applique l'effet puis retire l'objet de l'inventaire (usage unique).
     *
     * @param cible le joueur qui utilise le consommable
     */
    @Override
    public void utiliser(Object cible) {
        if (cible instanceof Joueur joueur) {
            appliquerEffet(joueur);
            // Retire l'objet après utilisation (consommable = usage unique)
            joueur.getInventaire().retirerObjet(this);
        }
    }

    /**
     * Applique l'effet du consommable au joueur.
     * Peut être surchargée par les sous-classes (ex: Potion).
     *
     * @param joueur le joueur qui reçoit l'effet
     */
    public void appliquerEffet(Joueur joueur) {
        System.out.println("  " + nom + " utilisé — effet : " + effet);
    }

    // ── Getters ────────────────────────────────────────────

    /** @return le type d'effet du consommable */
    public String getEffet()       { return effet; }

    /** @return l'intensité de l'effet */
    public int    getValeurEffet() { return valeurEffet; }
}