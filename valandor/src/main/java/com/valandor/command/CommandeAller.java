package com.valandor.command;

import com.valandor.world.Lieu;

/**
 * Commande de deplacement vers un lieu adjacent.
 * Verifie que la sortie existe dans la direction demandee
 * avant de deplacer le joueur.
 *
 * Exemple d'utilisation : "nord", "aller sud", "e"
 */
public class CommandeAller implements Commande {

    // ── Attributs ──────────────────────────────────────────
    /** Direction vers laquelle se deplacer (nord/sud/est/ouest) */
    private String direction;

    // ── Constructeur ───────────────────────────────────────

    /**
     * @param direction la direction de deplacement
     */
    public CommandeAller(String direction) {
        this.direction = direction;
    }

    // ── Execution ──────────────────────────────────────────

    /**
     * Verifie si une sortie existe dans la direction donnee.
     * Si oui, deplace le joueur et decrit le nouveau lieu.
     * Si non, affiche un message d'erreur.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        Lieu lieu = contexte.getLieuCourant().getSortie(direction);
        if (lieu == null) {
            System.out.println("  Impossible d'aller vers : " + direction);
            return;
        }
        // Deplace le joueur vers le nouveau lieu
        contexte.setLieuCourant(lieu);
        // Affiche la description du nouveau lieu
        lieu.decrire();
    }
}