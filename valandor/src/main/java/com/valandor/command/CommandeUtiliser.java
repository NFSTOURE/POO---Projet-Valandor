package com.valandor.command;

public class CommandeUtiliser implements Commande {

    private String nomObjet;

    public CommandeUtiliser(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().utiliserObjet(nomObjet);
    }
}