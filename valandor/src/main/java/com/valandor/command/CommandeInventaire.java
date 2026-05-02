package com.valandor.command;

/**
 * Commande pour afficher le contenu de l'inventaire du joueur.
 * Montre tous les objets avec leur poids et le poids total utilise.
 *
 * Exemple d'utilisation : "inventaire", "i"
 */
public class CommandeInventaire implements Commande {

    /**
     * Delegue l'affichage a la methode afficher() de l'Inventaire.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().getInventaire().afficher();
    }
}