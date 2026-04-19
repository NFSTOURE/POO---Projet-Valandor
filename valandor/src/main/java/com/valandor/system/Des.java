package com.valandor.system;

import java.util.Random;

/**
 * Simule des lancers de dés pour le système de combat.
 * Encapsule la génération de nombres aléatoires dans une classe dédiée
 * pour centraliser la logique aléatoire et faciliter les tests unitaires.
 *
 * Dés disponibles : D6 (6 faces), D20 (20 faces), et tout nombre de faces.
 * Le constructeur avec seed permet des résultats reproductibles pour les tests.
 *
 * Utilisation en combat :
 * - D20 pour déterminer si l'attaque touche (>= 5 = succès)
 * - Dés de dégâts selon l'arme (ex: 1D6+2 pour l'épée rouillée)
 */
public class Des {

    // ── Attributs ──────────────────────────────────────────
    private Random aleatoire; // générateur de nombres aléatoires

    // ── Constructeurs ──────────────────────────────────────

    /**
     * Crée un générateur de dés avec une graine aléatoire.
     * Utilisé en jeu normal pour des résultats imprévisibles.
     */
    public Des() {
        this.aleatoire = new Random();
    }

    /**
     * Crée un générateur de dés avec une graine fixe.
     * Utilisé dans les tests unitaires pour des résultats reproductibles.
     * Deux instances avec la même graine donnent les mêmes séquences.
     *
     * @param seed graine du générateur (même seed = mêmes résultats)
     */
    public Des(long seed) {
        this.aleatoire = new Random(seed);
    }

    // ── Méthodes de lancer ─────────────────────────────────

    /**
     * Lance un dé à N faces et retourne le résultat.
     * Résultat toujours entre 1 et faces (inclus).
     *
     * @param faces nombre de faces du dé (minimum 2)
     * @return résultat du lancer entre 1 et faces
     * @throws IllegalArgumentException si faces < 2
     */
    public int lancer(int faces) {
        if (faces < 2) {
            throw new IllegalArgumentException(
                    "Un dé doit avoir au moins 2 faces, reçu : " + faces);
        }
        // nextInt(faces) retourne [0, faces-1], +1 donne [1, faces]
        return aleatoire.nextInt(faces) + 1;
    }

    /**
     * Lance plusieurs dés identiques et retourne la somme.
     * Ex: lancerMultiple(2, 6) simule un lancer de 2D6.
     *
     * @param nombre nombre de dés à lancer
     * @param faces  nombre de faces de chaque dé
     * @return somme de tous les lancers
     */
    public int lancerMultiple(int nombre, int faces) {
        int total = 0;
        for (int i = 0; i < nombre; i++) {
            total += lancer(faces);
        }
        return total;
    }

    /**
     * Lance un dé à 6 faces (D6).
     * Utilisé pour les dégâts des armes basiques.
     *
     * @return résultat entre 1 et 6
     */
    public int lancerD6() {
        return lancer(6);
    }

    /**
     * Lance un dé à 20 faces (D20).
     * Utilisé pour déterminer si une attaque touche sa cible.
     * Seuil de réussite : >= 5 (défini dans CommandeAttaquer)
     *
     * @return résultat entre 1 et 20
     */
    public int lancerD20() {
        return lancer(20);
    }
}