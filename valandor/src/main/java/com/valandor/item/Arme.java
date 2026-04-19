package com.valandor.item;

import com.valandor.entity.Joueur;
import com.valandor.system.Des;

/**
 * Représente une arme équipable par le joueur.
 * Étend Objet et ajoute :
 * - Un système de dégâts basé sur des lancers de dés (ex: 1D6+2)
 * - Un type d'attaque (tranchant, contondant, magique)
 *
 * Relation POO : Arme "est un" Objet (héritage)
 * En combat : les dégâts = lancerDes(nbDes, facesDes) + degatsBonus
 */
public class Arme extends Objet {

    // ── Attributs spécifiques à l'arme ─────────────────────
    private int    degatsBonus;  // bonus fixe ajouté aux dégâts des dés
    private int    nbDes;        // nombre de dés lancés (ex: 2 pour 2D6)
    private int    facesDes;     // faces par dé (ex: 6 pour D6, 8 pour D8)
    private String typeAttaque;  // "tranchant", "contondant", "magique"
    private Des    des;          // utilitaire de lancer de dés

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée une arme avec ses caractéristiques de combat.
     *
     * @param nom         nom de l'arme
     * @param description description courte
     * @param poids       poids en kg
     * @param degatsBonus bonus fixe ajouté aux dégâts
     * @param nbDes       nombre de dés lancés pour les dégâts
     * @param facesDes    nombre de faces de chaque dé
     * @param typeAttaque type de dégâts ("tranchant", "contondant", "magique")
     */
    public Arme(String nom, String description, float poids,
                int degatsBonus, int nbDes, int facesDes, String typeAttaque) {
        super(nom, description, poids, degatsBonus);
        this.degatsBonus  = degatsBonus;
        this.nbDes        = nbDes;
        this.facesDes     = facesDes;
        this.typeAttaque  = typeAttaque;
        this.des          = new Des();
    }

    // ── Calcul des dégâts ──────────────────────────────────

    /**
     * Lance les dés et calcule les dégâts totaux de l'arme.
     * Formule : somme(nbDes × facesDes) + degatsBonus
     * Ex: épée longue (2D6+5) → lance 2 dés à 6 faces et ajoute 5
     *
     * @return les dégâts totaux de ce lancer
     */
    public int getLancerDegats() {
        int total = des.lancerMultiple(nbDes, facesDes) + degatsBonus;
        System.out.printf("  Lancer de dégâts : %dD%d + %d bonus = %d%n",
                nbDes, facesDes, degatsBonus, total);
        return total;
    }

    // ── Utilisation ────────────────────────────────────────

    /**
     * Équipe cette arme sur le joueur dans l'emplacement "arme".
     * Utilise le pattern visitor : l'objet sait comment s'appliquer au joueur.
     *
     * @param cible le joueur qui équipe l'arme
     */
    @Override
    public void utiliser(Object cible) {
        if (cible instanceof Joueur joueur) {
            joueur.equiper(this, "arme");
            System.out.println("  " + nom + " équipée ! (" + typeAttaque + ")");
        }
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Retourne une description détaillée de l'arme.
     * Format : Nom | NbDesD Faces+Bonus | Type | Poids kg
     */
    @Override
    public String toString() {
        return String.format("%s | %dD%d+%d | %s | %.1f kg",
                nom, nbDes, facesDes, degatsBonus, typeAttaque, poids);
    }

    // ── Getters ────────────────────────────────────────────

    /** @return le bonus de dégâts fixe de l'arme */
    public int    getDegatsBonus()  { return degatsBonus; }

    /** @return le nombre de dés lancés */
    public int    getNbDes()        { return nbDes; }

    /** @return le nombre de faces de chaque dé */
    public int    getFacesDes()     { return facesDes; }

    /** @return le type d'attaque de l'arme */
    public String getTypeAttaque()  { return typeAttaque; }
}