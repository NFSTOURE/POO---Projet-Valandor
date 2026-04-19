package com.valandor.command;

import com.valandor.item.Objet;

public class CommandePrendre implements Commande {

    private String nomObjet;

    public CommandePrendre(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        Objet objet = contexte.getLieuCourant().trouverObjetParNom(nomObjet);
        if (objet == null) {
            System.out.println("  Objet introuvable ici : " + nomObjet);
            return;
        }
        boolean ajoute = contexte.getJoueur().getInventaire().ajouterObjet(objet);
        if (ajoute) {
            contexte.getLieuCourant().retirerObjet(objet);
        }
    }
}