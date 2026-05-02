package com.valandor.command;

import com.valandor.entity.EntiteVivante;
import com.valandor.entity.PNJ;

/**
 * Commande pour dialoguer avec un PNJ present dans le lieu actuel.
 * Recherche le PNJ par son nom (recherche partielle et insensible
 * aux accents), puis declenche le dialogue sur le sujet demande.
 * Si le sujet est "quete", propose automatiquement les quetes
 * disponibles du PNJ au joueur.
 *
 * Exemple d'utilisation :
 * "parler forgeron" → salutation par defaut
 * "parler forgeron quete" → propose la quete
 * "parler sage ruines" → dialogue sur les ruines
 */
public class CommandeParler implements Commande {

    /** Nom ou partie du nom du PNJ */
    private String nomPNJ;

    /** Sujet de la conversation */
    private String sujet;

    /**
     * @param nomPNJ nom ou partie du nom du PNJ
     * @param sujet  sujet de la conversation
     */
    public CommandeParler(String nomPNJ, String sujet) {
        this.nomPNJ = nomPNJ;
        this.sujet  = sujet;
    }

    /**
     * Recherche le PNJ dans le lieu courant.
     * Verifie que l'entite trouvee est bien un PNJ (pas un ennemi).
     * Declenche le dialogue et propose les quetes si sujet = "quete".
     */
    @Override
    public void executer(ContexteJeu contexte) {
        // Recherche de l'entite par nom partiel
        EntiteVivante entite = contexte.getLieuCourant()
                .trouverEntiteParNom(nomPNJ);

        if (entite == null) {
            System.out.println("  Personne ne s'appelle "
                    + nomPNJ + " ici.");
            return;
        }

        // Verifie que l'entite est un PNJ dialoguable
        if (!(entite instanceof PNJ pnj)) {
            System.out.println("  " + entite.getNom()
                    + " ne veut pas parler.");
            return;
        }

        // Declenche le dialogue
        pnj.parler(sujet);

        // Si le sujet est "quete", propose les quetes disponibles
        if (sujet.equalsIgnoreCase("quete")) {
            pnj.getQuetesDisponibles().forEach(q -> {
                if (!contexte.getJoueur().getQuetes().contains(q)) {
                    contexte.getJoueur().accepterQuete(q);
                }
            });
        }
    }
}