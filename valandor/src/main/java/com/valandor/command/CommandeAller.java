package com.valandor.command;

import com.valandor.world.Lieu;

public class CommandeAller implements Commande {

    private String direction;

    public CommandeAller(String direction) {
        this.direction = direction;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        Lieu lieu = contexte.getLieuCourant().getSortie(direction);
        if (lieu == null) {
            System.out.println("  Impossible d'aller vers : " + direction);
            return;
        }
        contexte.setLieuCourant(lieu);
        lieu.decrire();
    }
}