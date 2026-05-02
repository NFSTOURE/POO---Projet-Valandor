package com.valandor.command;

/**
 * Commande pour afficher la liste de toutes les commandes disponibles.
 * Organisee par categories : deplacement, objets, combat,
 * interactions, informations, sauvegarde.
 *
 * Exemple d'utilisation : "aide", "?"
 */
public class CommandeAide implements Commande {

    /**
     * Affiche un tableau structure de toutes les commandes
     * avec leurs alias et leur description.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        System.out.println("\n  ==========================================");
        System.out.println("           COMMANDES DISPONIBLES");
        System.out.println("  ==========================================");
        System.out.println("  DEPLACEMENT");
        System.out.println("    nord / sud / est / ouest");
        System.out.println("    aller [direction]");
        System.out.println("  OBJETS");
        System.out.println("    prendre [objet]");
        System.out.println("    utiliser [objet]");
        System.out.println("    equiper [objet]");
        System.out.println("    desequiper [arme/armure/casque]");
        System.out.println("    regarder [objet]");
        System.out.println("  COMBAT");
        System.out.println("    attaquer [ennemi]");
        System.out.println("  INTERACTIONS");
        System.out.println("    parler [pnj]");
        System.out.println("    parler [pnj] [sujet]");
        System.out.println("  INFORMATIONS");
        System.out.println("    inventaire / i");
        System.out.println("    statut / st");
        System.out.println("    quetes / q / journal");
        System.out.println("  SAUVEGARDE");
        System.out.println("    sauvegarder / save");
        System.out.println("    charger");
        System.out.println("  AUTRE");
        System.out.println("    aide / ?");
        System.out.println("    quitter");
        System.out.println("  ==========================================");
    }
}