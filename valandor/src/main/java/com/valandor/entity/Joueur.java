package com.valandor.entity;

import com.valandor.item.Objet;
import com.valandor.system.Inventaire;
import com.valandor.world.Quete;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente le personnage contrôlé par le joueur.
 * Étend EntiteVivante et ajoute :
 * - Un système de progression (XP et niveaux)
 * - Un inventaire (composition : le joueur "possède" un inventaire)
 * - Des emplacements d'équipement (arme, armure, casque)
 * - Un journal de quêtes
 *
 * Relation POO : Joueur "est une" EntiteVivante (héritage)
 *                Joueur "possède" un Inventaire (composition)
 */
public class Joueur extends EntiteVivante {

    // ── Attributs spécifiques au joueur ────────────────────
    private int                niveau;      // niveau actuel (commence à 1)
    private int                xp;          // expérience accumulée
    private Inventaire         inventaire;  // objets portés (composition)
    private Map<String, Objet> equipement;  // objets équipés par emplacement
    private List<Quete>        quetes;      // journal de quêtes
    private String             lieuActuel;  // identifiant du lieu courant

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un nouveau joueur avec les stats de départ.
     * Stats initiales : 100 PV, 10 ATT, 5 DEF, niveau 1.
     *
     * @param nom nom du personnage choisi par le joueur
     */
    public Joueur(String nom) {
        super(nom, 100, 10, 5);
        this.xp         = 0;
        this.niveau     = 1;
        this.inventaire = new Inventaire(50.0f); // capacité max 50 kg
        this.equipement = new HashMap<>();
        this.quetes     = new ArrayList<>();
        this.lieuActuel = "village";
    }

    // ── Système de progression ─────────────────────────────

    /**
     * Ajoute de l'XP au joueur et vérifie si une montée de niveau est déclenchée.
     * La courbe de progression : niveau N → N+1 requiert N×100 XP.
     *
     * @param quantite points d'expérience gagnés
     */
    public void gagnerXP(int quantite) {
        this.xp += quantite;
        System.out.printf("  %s gagne %d XP (total : %d XP)%n", nom, quantite, xp);
        verifierMonteeNiveau();
    }

    /**
     * Vérifie si le seuil d'XP pour monter de niveau est atteint.
     * Si oui, déclenche la montée de niveau et soustrait l'XP utilisée.
     */
    private void verifierMonteeNiveau() {
        int xpNecessaire = calculerXPNecessaire();
        if (xp >= xpNecessaire) {
            xp -= xpNecessaire;
            monterNiveau();
        }
    }

    /**
     * Calcule l'XP nécessaire pour passer au niveau suivant.
     * Formule : niveau actuel × 100 (progression linéaire).
     */
    private int calculerXPNecessaire() {
        return niveau * 100;
    }

    /**
     * Applique la montée de niveau :
     * - Incrémente le niveau
     * - Augmente les stats (PV +20, ATT +3, DEF +2)
     * - Restaure tous les PV (récompense de la montée de niveau)
     */
    private void monterNiveau() {
        niveau++;
        pvMax      += 20;
        pvCourants  = pvMax; // restauration complète des PV
        attaque    += 3;
        defense    += 2;
        System.out.println("  *** NIVEAU " + niveau + " ! ***");
        System.out.println("  " + getStats());
    }

    // ── Système d'équipement ───────────────────────────────

    /**
     * Équipe un objet dans un emplacement donné.
     * Si un objet est déjà équipé à cet emplacement, il est renvoyé dans l'inventaire.
     * L'objet équipé est retiré de l'inventaire.
     *
     * @param objet      objet à équiper
     * @param emplacement "arme", "armure" ou "casque"
     */
    public void equiper(Objet objet, String emplacement) {
        // Déséquipe l'objet précédent si nécessaire
        if (equipement.containsKey(emplacement)) {
            Objet ancien = equipement.get(emplacement);
            inventaire.ajouterObjet(ancien);
            System.out.println("  " + ancien.getNom() + " déséquipé.");
        }
        equipement.put(emplacement, objet);
        inventaire.retirerObjet(objet);
        System.out.println("  " + objet.getNom() + " équipé à : " + emplacement);
    }

    /**
     * Déséquipe l'objet d'un emplacement et le remet dans l'inventaire.
     *
     * @param emplacement "arme", "armure" ou "casque"
     */
    public void desequiper(String emplacement) {
        if (!equipement.containsKey(emplacement)) {
            System.out.println("  Aucun objet équipé à : " + emplacement);
            return;
        }
        Objet objet = equipement.remove(emplacement);
        inventaire.ajouterObjet(objet);
        System.out.println("  " + objet.getNom() + " rangé dans l'inventaire.");
    }

    /**
     * Calcule le bonus d'attaque total provenant de l'équipement.
     * Additionne la valeur de tous les objets équipés.
     */
    public int getBonusAttaque() {
        return equipement.values().stream()
                .mapToInt(Objet::getValeur)
                .sum();
    }

    // ── Utilisation d'objets ───────────────────────────────

    /**
     * Utilise un objet de l'inventaire par son nom.
     * L'objet applique son effet puis est retiré si c'est un consommable.
     *
     * @param nomObjet nom de l'objet à utiliser
     */
    public void utiliserObjet(String nomObjet) {
        Objet objet = inventaire.trouverParNom(nomObjet);
        if (objet == null) {
            System.out.println("  Objet introuvable : " + nomObjet);
            return;
        }
        objet.utiliser(this);
    }

    // ── Système de quêtes ──────────────────────────────────

    /**
     * Accepte une nouvelle quête et l'ajoute au journal.
     * Vérifie que la quête n'est pas déjà acceptée.
     *
     * @param quete la quête à accepter
     */
    public void accepterQuete(Quete quete) {
        if (quetes.contains(quete)) {
            System.out.println("  Quête déjà acceptée : " + quete.getTitre());
            return;
        }
        quetes.add(quete);
        quete.accepter(this);
    }

    /**
     * Vérifie toutes les quêtes en cours et les termine si leurs
     * conditions sont remplies.
     */
    public void verifierQuetes() {
        quetes.stream()
              .filter(q -> !q.estTerminee() && q.estAcceptee())
              .forEach(q -> q.verifierEtTerminer(this));
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Affiche un tableau récapitulatif des stats du joueur :
     * niveau, XP, PV, attaque, défense.
     */
    public void afficherStatuts() {
        System.out.println("  ╔══════════════════════════╗");
        System.out.printf ("  ║ %-26s║%n", nom);
        System.out.printf ("  ║ Niveau : %-17d║%n", niveau);
        System.out.printf ("  ║ XP     : %-5d / %-10d║%n", xp, calculerXPNecessaire());
        System.out.printf ("  ║ PV     : %-5d / %-10d║%n", pvCourants, pvMax);
        System.out.printf ("  ║ ATT    : %-17d║%n", attaque);
        System.out.printf ("  ║ DEF    : %-17d║%n", defense);
        System.out.println("  ╚══════════════════════════╝");
    }

    // ── Implémentation de la méthode abstraite ─────────────

    /**
     * Le joueur agit via les commandes saisies au clavier.
     * Cette méthode est appelée par le moteur de jeu à chaque tour.
     * La logique réelle est dans les classes Command (patron Command).
     */
    @Override
    public void agir(Object contexte) {
        System.out.println("En attente de commande...");
    }

    // ── Getters & Setters ──────────────────────────────────

    /** @return l'XP accumulée depuis le dernier niveau */
    public int getXp()          { return xp; }

    /** @return le niveau actuel du joueur */
    public int getNiveau()      { return niveau; }

    /** @return l'inventaire du joueur (composition) */
    public Inventaire getInventaire()  { return inventaire; }

    /** @return la map des objets équipés (emplacement → objet) */
    public Map<String, Objet> getEquipement() { return equipement; }

    /** @return la liste des quêtes du journal */
    public List<Quete> getQuetes()     { return quetes; }

    /** @return l'identifiant du lieu actuel */
    public String getLieuActuel()      { return lieuActuel; }

    /** Modifie le lieu actuel (utilisé lors du chargement) */
    public void setLieuActuel(String lieu) { this.lieuActuel = lieu; }
}