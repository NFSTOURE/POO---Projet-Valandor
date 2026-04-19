package com.valandor.command;

public class CommandeStatut implements Commande {

    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().afficherStatuts();
    }
}