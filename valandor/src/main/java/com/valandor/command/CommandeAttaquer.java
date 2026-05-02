package com.valandor.command;

import com.valandor.entity.Ennemi;
import com.valandor.entity.EntiteVivante;
import com.valandor.entity.Joueur;
import com.valandor.item.Arme;
import com.valandor.item.Objet;
import com.valandor.system.Des;

/**
 * Commande de combat contre un ennemi present dans le lieu actuel.
 * Implemente le systeme de combat tour par tour avec lancers de des :
 *
 * Tour du joueur :
 * 1. Lancer D20 pour toucher (>= 5 = succes)
 * 2. Si touche : lancer les des de l'arme equipee + ATT du joueur
 * 3. L'ennemi subit les degats (reduits par sa DEF)
 *
 * Tour de l'ennemi :
 * 4. Si l'ennemi est encore vivant, il contre-attaque
 * 5. Le joueur subit les degats (reduits par sa DEF)
 *
 * Fin du combat :
 * - Ennemi vaincu : XP + butin + notification des quetes
 * - Joueur mort   : fin de partie
 *
 * Exemple d'utilisation : "attaquer gobelin", "att chef"
 */
public class CommandeAttaquer implements Commande {

    // ── Attributs ──────────────────────────────────────────
    private String nomCible; // nom de l'ennemi a attaquer
    private Des    des;      // utilitaire de lancer de des

    // ── Constructeur ───────────────────────────────────────

    /**
     * @param nomCible nom de l'ennemi a attaquer
     */
    public CommandeAttaquer(String nomCible) {
        this.nomCible = nomCible;
        this.des      = new Des();
    }

    // ── Execution ──────────────────────────────────────────

    /**
     * Execute un tour de combat complet contre l'ennemi cible.
     */
    @Override
    public void executer(ContexteJeu contexte) {
        Joueur joueur = contexte.getJoueur();

        // Recherche la cible dans le lieu courant
        EntiteVivante entite = contexte.getLieuCourant()
                .trouverEntiteParNom(nomCible);

        if (entite == null) {
            System.out.println("  Pas de cible nommee '" + nomCible + "' ici.");
            return;
        }

        // Verifie que la cible est bien un ennemi combattable
        if (!(entite instanceof Ennemi ennemi)) {
            System.out.println("  Vous ne pouvez pas attaquer "
                    + entite.getNom() + " !");
            return;
        }

        if (!ennemi.estVivant()) {
            System.out.println("  " + ennemi.getNom() + " est deja mort.");
            return;
        }

        // ── Tour du joueur ─────────────────────────────────
        System.out.println("\n  COMBAT : " + joueur.getNom()
                + " vs " + ennemi.getNom());
        System.out.println("  " + ennemi.getStats());

        // Lancer D20 pour determiner si l'attaque touche
        int d20 = des.lancerD20();
        System.out.println("  Lancer D20 pour toucher : " + d20);

        if (d20 < 5) {
            // Attaque ratee
            System.out.println("  Rate ! Votre attaque ne touche pas.");
        } else {
            // Attaque reussie : calcul des degats selon l'arme equipee
            int degats;
            Objet armeEquipee = joueur.getEquipement().get("arme");
            if (armeEquipee instanceof Arme arme) {
                // Utilise les des de l'arme + ATT du joueur
                degats = arme.getLancerDegats() + joueur.getAttaque();
            } else {
                // Attaque a mains nues : D6 + ATT de base
                degats = joueur.getAttaque() + des.lancerD6();
                System.out.println("  Attaque a mains nues : "
                        + degats + " degats.");
            }
            ennemi.subirDegats(degats);
        }

        // ── Tour de l'ennemi ───────────────────────────────
        if (ennemi.estVivant()) {
            // L'ennemi contre-attaque
            ennemi.jouerTour(joueur);

            // Verifie si le joueur est mort apres l'attaque
            if (!joueur.estVivant()) {
                System.out.println("\n  Vous etes mort... Fin de l'aventure.");
                contexte.terminer();
                return;
            }
        } else {
            // ── Ennemi vaincu ──────────────────────────────
            System.out.println("\n  " + ennemi.getNom() + " est vaincu !");

            // Distribution de l'XP
            joueur.gagnerXP(ennemi.getRecompenseXP());

            // Notification de progression des quetes
            String nomEnnemi = ennemi.getNom();
            joueur.getQuetes().forEach(q -> q.progresser(nomEnnemi));
            joueur.verifierQuetes();

            // Distribution du butin dans l'inventaire
            ennemi.deposerButin().forEach(
                    objet -> joueur.getInventaire().ajouterObjet(objet));

            // Retire l'ennemi du lieu
            contexte.getLieuCourant().retirerEntite(ennemi);
        }

        // Affiche l'etat du joueur apres le combat
        System.out.println("\n  " + joueur.getStats());
    }
}