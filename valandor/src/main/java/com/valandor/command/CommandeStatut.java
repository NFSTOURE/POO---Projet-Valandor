package com.valandor.command;

/**
 * Commande pour afficher les statistiques du personnage.
 * Montre le niveau, l'XP, les PV, l'attaque et la defense.
 *
 * Exemple d'utilisation : "statut", "st"
 */
public class CommandeStatut implements Commande {

    /**
     * Delegue l'affichage a la methode afficherStatuts() du Joueur.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().afficherStatuts();
    }
}