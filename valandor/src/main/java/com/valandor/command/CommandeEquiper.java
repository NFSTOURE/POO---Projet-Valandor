package com.valandor.command;

import com.valandor.item.Armure;
import com.valandor.item.Arme;
import com.valandor.item.Objet;

/**
 * Commande pour equiper un objet de l'inventaire.
 * Detecte automatiquement le type de l'objet (Arme ou Armure)
 * et appelle la methode utiliser() appropriee.
 *
 * Exemple d'utilisation : "equiper epee", "eq casque"
 */
public class CommandeEquiper implements Commande {

    /** Nom de l'objet a equiper */
    private String nomObjet;

    /**
     * @param nomObjet nom ou partie du nom de l'objet a equiper
     */
    public CommandeEquiper(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    /**
     * Recherche l'objet dans l'inventaire.
     * Si c'est une Arme ou une Armure, appelle utiliser()
     * qui se charge de l'equiper dans le bon emplacement.
     * Sinon affiche un message d'erreur.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        Objet objet = contexte.getJoueur()
                .getInventaire().trouverParNom(nomObjet);
        if (objet == null) {
            System.out.println("  Objet introuvable dans l'inventaire : "
                    + nomObjet);
            return;
        }
        // Verifie que l'objet est equipable
        if (objet instanceof Arme arme) {
            arme.utiliser(contexte.getJoueur());
        } else if (objet instanceof Armure armure) {
            armure.utiliser(contexte.getJoueur());
        } else {
            System.out.println("  " + nomObjet
                    + " ne peut pas etre equipe.");
        }
    }
}