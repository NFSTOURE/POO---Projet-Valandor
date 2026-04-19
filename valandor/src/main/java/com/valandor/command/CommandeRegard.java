package com.valandor.command;

import com.valandor.item.Objet;

public class CommandeRegard implements Commande {

    private String nomObjet;

    public CommandeRegard(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        // Cherche dans l'inventaire d'abord
        Objet objet = contexte.getJoueur().getInventaire().trouverParNom(nomObjet);

        // Sinon cherche dans le lieu
        if (objet == null) {
            objet = contexte.getLieuCourant().trouverObjetParNom(nomObjet);
        }

        if (objet == null) {
            System.out.println("  Impossible d'examiner : " + nomObjet);
            return;
        }
        System.out.println("  " + objet.toString());
    }
}