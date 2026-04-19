package com.valandor.command;

public class CommandeInventaire implements Commande {

    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().getInventaire().afficher();
    }
}