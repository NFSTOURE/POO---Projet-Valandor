package com.valandor.command;

import com.valandor.world.Quete;
import java.util.List;

public class CommandeQuetes implements Commande {

    @Override
    public void executer(ContexteJeu contexte) {
        List<Quete> quetes = contexte.getJoueur().getQuetes();
        if (quetes.isEmpty()) {
            System.out.println("  Aucune quete en cours.");
            return;
        }
        System.out.println("\n  ==========================================");
        System.out.println("             JOURNAL DE QUETES");
        System.out.println("  ==========================================");
        quetes.forEach(Quete::afficher);
        System.out.println("  ==========================================");
    }
}