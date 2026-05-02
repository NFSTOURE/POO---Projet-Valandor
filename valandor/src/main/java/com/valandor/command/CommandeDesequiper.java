package com.valandor.command;

/**
 * Commande pour desequiper un objet d'un emplacement donne.
 * L'objet est automatiquement remis dans l'inventaire du joueur.
 *
 * Emplacements disponibles : "arme", "armure", "casque"
 * Exemple d'utilisation : "desequiper arme", "deseq casque"
 */
public class CommandeDesequiper implements Commande {

    /** Emplacement a desequiper (arme, armure, casque) */
    private String emplacement;

    /**
     * @param emplacement l'emplacement a desequiper
     */
    public CommandeDesequiper(String emplacement) {
        this.emplacement = emplacement;
    }

    /**
     * Desequipe l'objet de l'emplacement indique.
     * L'objet est remis dans l'inventaire automatiquement.
     * Affiche un message si l'emplacement est vide.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().desequiper(emplacement);
    }
}