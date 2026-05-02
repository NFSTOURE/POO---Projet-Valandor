package com.valandor.command;

/**
 * Commande pour utiliser un objet de l'inventaire du joueur.
 * Delegue l'effet a la methode utiliserObjet() du joueur
 * qui appelle a son tour utiliser() sur l'objet concerne.
 *
 * Exemple d'utilisation : "utiliser potion", "u herbes"
 */
public class CommandeUtiliser implements Commande {

    /** Nom de l'objet a utiliser */
    private String nomObjet;

    /**
     * @param nomObjet nom ou partie du nom de l'objet a utiliser
     */
    public CommandeUtiliser(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    /**
     * Demande au joueur d'utiliser l'objet.
     * L'objet applique son effet (soin, boost, equipement)
     * et disparait de l'inventaire s'il est consommable.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().utiliserObjet(nomObjet);
    }
}