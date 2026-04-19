package com.valandor.command;

import com.valandor.item.Armure;
import com.valandor.item.Arme;
import com.valandor.item.Objet;

public class CommandeEquiper implements Commande {

    private String nomObjet;

    public CommandeEquiper(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        Objet objet = contexte.getJoueur().getInventaire().trouverParNom(nomObjet);
        if (objet == null) {
            System.out.println("  Objet introuvable dans l'inventaire : " + nomObjet);
            return;
        }
        if (objet instanceof Arme arme) {
            arme.utiliser(contexte.getJoueur());
        } else if (objet instanceof Armure armure) {
            armure.utiliser(contexte.getJoueur());
        } else {
            System.out.println("  " + nomObjet + " ne peut pas etre equipe.");
        }
    }
}