package com.valandor.command;

import com.valandor.entity.Ennemi;
import com.valandor.entity.EntiteVivante;
import com.valandor.entity.Joueur;
import com.valandor.item.Arme;
import com.valandor.item.Objet;
import com.valandor.system.Des;

public class CommandeAttaquer implements Commande {

    private String nomCible;
    private Des    des;

    public CommandeAttaquer(String nomCible) {
        this.nomCible = nomCible;
        this.des      = new Des();
    }

    @Override
    public void executer(ContexteJeu contexte) {
        Joueur joueur = contexte.getJoueur();

        // Cherche la cible dans le lieu
        EntiteVivante entite = contexte.getLieuCourant()
                .trouverEntiteParNom(nomCible);

        if (entite == null) {
            System.out.println("  Pas de cible nommée '" + nomCible + "' ici.");
            return;
        }

        if (!(entite instanceof Ennemi ennemi)) {
            System.out.println("  Vous ne pouvez pas attaquer " + entite.getNom() + " !");
            return;
        }

        if (!ennemi.estVivant()) {
            System.out.println("  " + ennemi.getNom() + " est déjà mort.");
            return;
        }

        // ── Tour du joueur ─────────────────────────────────
        System.out.println("\n  ⚔ COMBAT : " + joueur.getNom()
                + " vs " + ennemi.getNom() + " ⚔");
        System.out.println("  " + ennemi.getStats());

        // Lancer D20 pour toucher (>= 5 = succès)
        int d20 = des.lancerD20();
        System.out.println("  Lancer D20 pour toucher : " + d20);

        if (d20 < 5) {
            System.out.println("  Raté ! Votre attaque ne touche pas.");
        } else {
            // Calcul des dégâts : arme équipée ou attaque de base
            int degats;
            Objet armeEquipee = joueur.getEquipement().get("arme");
            if (armeEquipee instanceof Arme arme) {
                degats = arme.getLancerDegats() + joueur.getAttaque();
            } else {
                degats = joueur.getAttaque() + des.lancerD6();
                System.out.println("  Attaque à mains nues : " + degats + " dégâts.");
            }
            ennemi.subirDegats(degats);
        }

        // ── Tour de l'ennemi ───────────────────────────────
        if (ennemi.estVivant()) {
            ennemi.jouerTour(joueur);

            // Vérifie si le joueur est mort
            if (!joueur.estVivant()) {
                System.out.println("\n  Vous êtes mort... Fin de l'aventure.");
                contexte.terminer();
                return;
            }
        } else {
            // Ennemi vaincu
            System.out.println("\n  " + ennemi.getNom() + " est vaincu !");
            joueur.gagnerXP(ennemi.getRecompenseXP());
            // Notifie les quetes actives
            joueur.verifierQuetes();
            String nomEnnemi = ennemi.getNom();
            joueur.getQuetes().forEach(q -> q.progresser(nomEnnemi));
            joueur.verifierQuetes();

            // Butin
            ennemi.deposerButin().forEach(objet -> {
                joueur.getInventaire().ajouterObjet(objet);
            });

            // Retire l'ennemi du lieu
            contexte.getLieuCourant().retirerEntite(ennemi);
        }

        // Affiche l'état du joueur après le combat
        System.out.println("\n  " + joueur.getStats());
    }
}