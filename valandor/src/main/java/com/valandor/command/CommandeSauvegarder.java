package com.valandor.command;

import com.valandor.system.GestionSauvegarde;

/**
 * Commande pour sauvegarder l'etat de la partie en JSON.
 * Le nom du fichier est genere automatiquement
 * a partir du nom du personnage (en minuscules, sans espaces).
 *
 * Exemple : joueur "Fatou" → fichier "saves/fatou.json"
 * Exemple d'utilisation : "sauvegarder", "save"
 */
public class CommandeSauvegarder implements Commande {

    /**
     * Genere le nom du fichier depuis le nom du joueur
     * et delegue la sauvegarde a GestionSauvegarde.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        // Nom de fichier : nom du joueur en minuscules sans espaces
        String nomFichier = contexte.getJoueur().getNom()
                .toLowerCase()
                .replaceAll("\\s+", "_");
        GestionSauvegarde.sauvegarder(contexte.getJoueur(), nomFichier);
    }
}