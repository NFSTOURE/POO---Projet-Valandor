package com.valandor.command;

import com.valandor.entity.EntiteVivante;
import com.valandor.entity.PNJ;

public class CommandeParler implements Commande {

    private String nomPNJ;
    private String sujet;

    public CommandeParler(String nomPNJ, String sujet) {
        this.nomPNJ = nomPNJ;
        this.sujet  = sujet;
    }

    @Override
    public void executer(ContexteJeu contexte) {
        EntiteVivante entite = contexte.getLieuCourant()
                .trouverEntiteParNom(nomPNJ);

        if (entite == null) {
            System.out.println("  Personne ne s'appelle " + nomPNJ + " ici.");
            return;
        }

        if (!(entite instanceof PNJ pnj)) {
            System.out.println("  " + nomPNJ + " ne veut pas parler.");
            return;
        }
        pnj.parler(sujet);
        
        // Propose la quete si le sujet est "quete"
        if (sujet.equalsIgnoreCase("quete")) {
            pnj.getQuetesDisponibles().forEach(q -> {
                if (!contexte.getJoueur().getQuetes().contains(q)) {
                    contexte.getJoueur().accepterQuete(q);
                }
            });
        }
        
    }
}