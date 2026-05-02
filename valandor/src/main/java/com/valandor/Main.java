package com.valandor;

import com.valandor.command.Commande;
import com.valandor.command.ContexteJeu;
import com.valandor.entity.Ennemi;
import com.valandor.entity.Joueur;
import com.valandor.entity.PNJ;
import com.valandor.item.Arme;
import com.valandor.item.Armure;
import com.valandor.item.Potion;
import com.valandor.system.AnalyseurCommandes;
import com.valandor.system.GestionSauvegarde;
import com.valandor.world.Lieu;
import com.valandor.world.Quete;
import java.util.Scanner;

/**
 * Point d'entree principal du jeu Les Chroniques de Valandor.
 * Responsabilites :
 * - Construire le monde (lieux, connexions, objets, ennemis, PNJ)
 * - Creer et configurer les quetes
 * - Initialiser le joueur et le contexte de jeu
 * - Gerer la boucle principale du jeu (lire → analyser → executer)
 *
 * Architecture utilisee :
 * - Patron Command : chaque entree joueur → objet Commande
 * - Composition    : Joueur possede Inventaire, EquipementSlots
 * - Heritage       : EntiteVivante → Joueur/Ennemi/PNJ
 */
public class Main {

    public static void main(String[] args) {

        // ══════════════════════════════════════════
        // 1. CREATION DU MONDE
        // ══════════════════════════════════════════

        // Creation des 5 lieux du monde
        Lieu village = new Lieu("Village de Valandor",
                "Un petit village paisible. Les habitants vaquent"
                + " a leurs occupations.");
        Lieu foret   = new Lieu("Foret Sombre",
                "Les arbres bloquent la lumiere."
                + " Des bruits inquietants resonnent.");
        Lieu caverne = new Lieu("Caverne des Gobelins",
                "Une caverne humide. Des torches eclairent"
                + " faiblement les murs.");
        Lieu marche  = new Lieu("Marche du village",
                "Un marche anime. Des marchands proposent"
                + " toutes sortes d'objets.");
        Lieu ruines  = new Lieu("Ruines Anciennes",
                "Des ruines mysterieuses. Une aura sombre y regne.");

        // Connexions bidirectionnelles entre les lieux
        village.ajouterSortie("nord",  foret);
        village.ajouterSortie("est",   marche);
        foret.ajouterSortie("sud",     village);
        foret.ajouterSortie("est",     caverne);
        foret.ajouterSortie("nord",    ruines);
        caverne.ajouterSortie("ouest", foret);
        marche.ajouterSortie("ouest",  village);
        ruines.ajouterSortie("sud",    foret);

        // ══════════════════════════════════════════
        // 2. OBJETS DANS LE MONDE
        // ══════════════════════════════════════════

        // Village : equipement de depart accessible
        Arme   epee       = new Arme("Epee rouilee",
                "Une vieille epee encore fonctionnelle.",
                3.0f, 2, 1, 6, "tranchant");
        Armure casque     = new Armure("Casque de fer",
                "Un casque solide.", 2.0f, 3, "casque");
        Potion potionSoin = new Potion("Potion de soin", 30, "soin");
        village.ajouterObjet(epee);
        village.ajouterObjet(casque);
        village.ajouterObjet(potionSoin);

        // Marche : equipement intermediaire
        Arme   epeeLongue  = new Arme("Epee longue",
                "Une epee bien forgee.", 4.0f, 5, 2, 6, "tranchant");
        Armure armureCuir  = new Armure("Armure de cuir",
                "Legere mais resistante.", 5.0f, 5, "armure");
        Potion potionForce = new Potion("Potion de force", 5, "force");
        marche.ajouterObjet(epeeLongue);
        marche.ajouterObjet(armureCuir);
        marche.ajouterObjet(potionForce);

        // Foret : ressource de soin
        Potion herbes = new Potion("Herbes medicinales", 20, "soin");
        foret.ajouterObjet(herbes);

        // Ruines : equipement rare de fin de jeu
        Arme epeeAncienne = new Arme("Epee ancienne",
                "Une epee magique trouvee dans les ruines.",
                3.5f, 8, 2, 8, "magique");
        ruines.ajouterObjet(epeeAncienne);

        // ══════════════════════════════════════════
        // 3. ENNEMIS
        // ══════════════════════════════════════════

        // Foret : 2 gobelins standards
        Ennemi gobelin1 = new Ennemi("Gobelin", 30, 8, 2, 50,
                "Un gobelin hostile.");
        Ennemi gobelin2 = new Ennemi("Gobelin", 30, 8, 2, 50,
                "Un gobelin hostile.");
        gobelin1.ajouterButin(new Potion("Petite potion", 15, "soin"));
        foret.ajouterEntite(gobelin1);
        foret.ajouterEntite(gobelin2);

        // Caverne : chef gobelin plus puissant
        Ennemi gobelinChef = new Ennemi("Chef Gobelin", 60, 12, 4,
                120, "Le chef des gobelins, plus puissant.");
        gobelinChef.ajouterButin(new Arme("Hache gobeline",
                "Une hache crude.", 4.0f, 4, 1, 8, "tranchant"));
        caverne.ajouterEntite(gobelinChef);

        // Ruines : squelette resistants
        Ennemi squelette = new Ennemi("Squelette", 40, 10, 6, 80,
                "Un squelette anime par une magie sombre.");
        squelette.ajouterButin(
                new Potion("Potion ancienne", 25, "soin"));
        ruines.ajouterEntite(squelette);

        // Marche : gobelin voleur
        Ennemi gobelinVoleur = new Ennemi("Gobelin voleur", 25, 7, 1,
                40, "Un gobelin qui rodait au marche.");
        marche.ajouterEntite(gobelinVoleur);

        // ══════════════════════════════════════════
        // 4. PNJ ET QUETES
        // ══════════════════════════════════════════

        // Forgeron : donne la quete des gobelins
        PNJ forgeron = new PNJ("Forgeron",
                "Bonjour voyageur ! Je forge les meilleures armes.");
        forgeron.ajouterDialogue("bonjour",
                "Bienvenue a Valandor, aventurier !");
        forgeron.ajouterDialogue("quete",
                "Elimine les gobelins de la foret !");
        forgeron.ajouterDialogue("arme",
                "Mon epee longue est au marche, va voir !");
        forgeron.ajouterDialogue("aide",
                "Va au nord vers la foret, ils sont la !");

        // Quete 1 : eliminer 2 gobelins
        Quete queteGoblins = new Quete(
                "Menace Gobeline",
                "Elimine les gobelins qui terrorisent la foret.",
                150, "Tuer 2 gobelins", "tuer", "Gobelin", 2);
        queteGoblins.ajouterRecompense(
                new Potion("Grande Potion", 50, "soin"));
        forgeron.ajouterQuete(queteGoblins);
        village.ajouterEntite(forgeron);

        // Vieux Sage : donne la quete des ruines
        PNJ sage = new PNJ("Vieux Sage",
                "Les ruines au nord de la foret cachent"
                + " de grands secrets...");
        sage.ajouterDialogue("bonjour",
                "Que la sagesse guide tes pas, aventurier.");
        sage.ajouterDialogue("ruines",
                "Mefie-toi des squelettes dans les ruines !");
        sage.ajouterDialogue("quete",
                "Rapporte-moi l'epee ancienne des ruines !");
        sage.ajouterDialogue("histoire",
                "Valandor fut jadis un grand royaume...");

        // Quete 2 : vaincre le squelette
        Quete queteRuines = new Quete(
                "Secret des Ruines",
                "Le sage veut que tu explores les ruines"
                + " et vainques le squelette.",
                200, "Vaincre le squelette des ruines",
                "tuer", "Squelette", 1);
        queteRuines.ajouterRecompense(
                new Potion("Potion de defense", 8, "defense"));
        sage.ajouterQuete(queteRuines);
        village.ajouterEntite(sage);

        // Marchand : informations sur les objets
        PNJ marchand = new PNJ("Marchand",
                "Bienvenue au marche ! Les meilleurs objets !");
        marchand.ajouterDialogue("bonjour",
                "Bonjour ! Tu cherches quelque chose ?");
        marchand.ajouterDialogue("objets",
                "J'ai une epee longue et une armure de cuir !");
        marchand.ajouterDialogue("quete",
                "Un gobelin voleur rode par ici, fais attention !");
        marche.ajouterEntite(marchand);

        // ══════════════════════════════════════════
        // 5. INITIALISATION DU JOUEUR
        // ══════════════════════════════════════════

        Scanner scanner = new Scanner(System.in);
        System.out.print("  Entrez le nom de votre personnage : ");
        String nomJoueur = scanner.nextLine().trim();
        if (nomJoueur.isBlank()) nomJoueur = "Heros";

        Joueur             joueur    = new Joueur(nomJoueur);
        ContexteJeu        contexte  = new ContexteJeu(joueur, village);
        AnalyseurCommandes analyseur = new AnalyseurCommandes();

        // ══════════════════════════════════════════
        // 6. INTRODUCTION
        // ══════════════════════════════════════════

        System.out.println("\n  ==========================================");
        System.out.println("       Les Chroniques de Valandor");
        System.out.println("  ==========================================");
        System.out.println("  Une menace obscure s'eveille...");
        System.out.println("  Tapez 'aide' pour voir les commandes.");
        System.out.println("  ==========================================\n");
        village.decrire();

        // ══════════════════════════════════════════
        // 7. BOUCLE PRINCIPALE DU JEU
        // ══════════════════════════════════════════

        while (contexte.isEnCours()) {
            System.out.print("\n> ");
            String entree = scanner.nextLine();

            // Commande quitter : termine la partie
            if (entree.equalsIgnoreCase("quitter")) {
                System.out.println("  A bientot dans Valandor !");
                contexte.terminer();
                break;
            }

            // Commande charger : charge une sauvegarde existante
            if (entree.equalsIgnoreCase("charger")) {
                GestionSauvegarde.listerSauvegardes();
                System.out.print("  Nom de la sauvegarde : ");
                String nomSave = scanner.nextLine().trim();
                Joueur joueurCharge = GestionSauvegarde.charger(nomSave);
                if (joueurCharge != null) {
                    // Remplace le joueur actuel par le joueur charge
                    contexte = new ContexteJeu(joueurCharge, village);
                    contexte.getLieuCourant().decrire();
                }
                continue;
            }

            // Analyse et execution de la commande via le patron Command
            Commande commande = analyseur.analyser(entree);
            if (commande != null) {
                commande.executer(contexte);
            }
        }

        scanner.close();
    }
}