package com.valandor.command;

/**
 * Commande qui décrit le lieu actuel où se trouve le joueur.
 * Déclenchée par "regarder" sans argument.
 * Affiche le nom, la description, les sorties, les objets
 * et les entités présentes dans le lieu courant.
 */
public class CommandeRegarderLieu implements Commande {

    /**
     * Exécute la description du lieu actuel.
     * Délègue l'affichage à la méthode decrire() de Lieu.
     *
     * @param contexte le contexte de jeu contenant le lieu courant
     */
    @Override
    public void executer(ContexteJeu contexte) {
        contexte.getLieuCourant().decrire();
    }
}