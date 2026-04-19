package com.valandor.command;

import com.valandor.system.GestionSauvegarde;

public class CommandeSauvegarder implements Commande {

    @Override
    public void executer(ContexteJeu contexte) {
        String nomFichier = contexte.getJoueur().getNom().toLowerCase()
                .replaceAll("\\s+", "_");
        GestionSauvegarde.sauvegarder(contexte.getJoueur(), nomFichier);
    }
}