package com.valandor.command;

/**
 * Interface definissant le contrat du patron de conception Commande.
 * Toute action du joueur est encapsulee dans une classe implementant
 * cette interface.
 *
 * Avantages du patron Command :
 * - Ajout de nouvelles commandes sans modifier le code existant
 *   (principe Ouvert/Ferme)
 * - Chaque commande a une responsabilite unique et claire
 * - Facilite les tests unitaires de chaque action independamment
 *
 * Implementations : CommandeAller, CommandeAttaquer, CommandePrendre,
 * CommandeUtiliser, CommandeEquiper, CommandeDesequiper, CommandeParler,
 * CommandeRegard, CommandeInventaire, CommandeStatut, CommandeQuetes,
 * CommandeSauvegarder, CommandeAide, CommandeRegarderLieu
 */
public interface Commande {

    /**
     * Execute l'action encapsulee par cette commande.
     * Recoit le contexte de jeu pour acceder au joueur,
     * au lieu courant et a l'etat global de la partie.
     *
     * @param contexte le contexte de jeu contenant toutes
     *                 les informations necessaires a l'execution
     */
    void executer(ContexteJeu contexte);
}