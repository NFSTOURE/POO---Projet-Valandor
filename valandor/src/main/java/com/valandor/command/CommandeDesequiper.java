package com.valandor.command;

public class CommandeDesequiper implements Commande {

    private String emplacement;

    public CommandeDesequiper(String emplacement) {
        this.emplacement = emplacement;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getJoueur().desequiper(emplacement);
    }
}