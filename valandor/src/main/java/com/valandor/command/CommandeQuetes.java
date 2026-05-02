package com.valandor.command;

import com.valandor.world.Quete;
import java.util.List;

/**
 * Commande pour afficher le journal de quetes du joueur.
 * Montre toutes les quetes avec leur statut et leur progression.
 *
 * Exemple d'utilisation : "quetes", "q", "journal"
 */
public class CommandeQuetes implements Commande {

    /**
     * Affiche toutes les quetes du joueur avec leur statut :
     * [DISPONIBLE], [EN COURS] ou [TERMINEE].
     * Affiche un message si aucune quete n'est en cours.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        List<Quete> quetes = contexte.getJoueur().getQuetes();
        if (quetes.isEmpty()) {
            System.out.println("  Aucune quete en cours.");
            System.out.println("  Parlez aux PNJ pour obtenir des quetes !");
            return;
        }
        System.out.println("\n  ==========================================");
        System.out.println("             JOURNAL DE QUETES");
        System.out.println("  ==========================================");
        quetes.forEach(Quete::afficher);
        System.out.println("  ==========================================");
    }
}