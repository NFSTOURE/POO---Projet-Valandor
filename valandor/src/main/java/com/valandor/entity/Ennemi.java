package com.valandor.entity;

import com.valandor.item.Objet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Représente un adversaire combattable dans le monde de Valandor.
 * Étend EntiteVivante et ajoute :
 * - Un système de récompense XP à la mort
 * - Un système de butin (objets lâchés aléatoirement)
 * - Un comportement automatique en combat (tour de l'ennemi)
 *
 * Relation POO : Ennemi "est une" EntiteVivante (héritage)
 */
public class Ennemi extends EntiteVivante {

    // ── Attributs spécifiques à l'ennemi ───────────────────
    private int         recompenseXP; // XP donnée au joueur à la mort
    private List<Objet> butin;        // objets pouvant être lâchés
    private float       chanceButin;  // probabilité de lâcher chaque objet (0.0 à 1.0)
    private String      description;  // description affichée au joueur
    private Random      aleatoire;    // générateur pour le butin aléatoire

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un nouvel ennemi avec ses caractéristiques de combat.
     *
     * @param nom          nom de l'ennemi
     * @param pvMax        points de vie maximum
     * @param attaque      puissance d'attaque
     * @param defense      absorption des dégâts
     * @param recompenseXP XP donnée au joueur quand l'ennemi est vaincu
     * @param description  description affichée au joueur
     */
    public Ennemi(String nom, int pvMax, int attaque, int defense,
                  int recompenseXP, String description) {
        super(nom, pvMax, attaque, defense);
        this.recompenseXP = recompenseXP;
        this.description  = description;
        this.butin        = new ArrayList<>();
        this.chanceButin  = 0.5f; // 50% de chance par défaut
        this.aleatoire    = new Random();
    }

    // ── Gestion du butin ───────────────────────────────────

    /**
     * Ajoute un objet à la liste de butin potentiel de l'ennemi.
     * Chaque objet a une chance indépendante d'être lâché.
     *
     * @param objet objet pouvant être lâché à la mort
     */
    public void ajouterButin(Objet objet) {
        butin.add(objet);
    }

    /**
     * Génère le butin lâché par l'ennemi à sa mort.
     * Chaque objet est tiré indépendamment selon chanceButin.
     *
     * @return liste des objets effectivement lâchés
     */
    public List<Objet> deposerButin() {
        List<Objet> butinDepose = new ArrayList<>();
        for (Objet objet : butin) {
            // Tirage aléatoire pour chaque objet du butin
            if (aleatoire.nextFloat() <= chanceButin) {
                butinDepose.add(objet);
                System.out.println("  " + nom + " a lâché : " + objet.getNom());
            }
        }
        return butinDepose;
    }

    // ── Comportement en combat ─────────────────────────────

    /**
     * Effectue le tour de l'ennemi en combat.
     * L'ennemi attaque automatiquement le joueur s'il est encore vivant.
     *
     * @param joueur le joueur ciblé par l'attaque
     */
    public void jouerTour(Joueur joueur) {
        if (!estVivant()) return;
        System.out.println("\n  " + nom + " passe à l'attaque !");
        attaquer(joueur);
    }

    // ── Implémentation de la méthode abstraite ─────────────

    /**
     * Comportement automatique de l'ennemi lors de son tour.
     * Attaque le joueur si le contexte contient un joueur valide.
     */
    @Override
    public void agir(Object contexte) {
        if (contexte instanceof Joueur) {
            jouerTour((Joueur) contexte);
        }
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Affiche la description et les stats de l'ennemi.
     */
    public void afficherDescription() {
        System.out.println("  " + nom + " — " + description);
        System.out.println("  " + getStats());
    }

    // ── Getters & Setters ──────────────────────────────────

    /** @return l'XP donnée au joueur à la mort de l'ennemi */
    public int getRecompenseXP()  { return recompenseXP; }

    /** @return la liste des objets de butin potentiel */
    public List<Objet> getButin() { return butin; }

    /** @return la description de l'ennemi */
    public String getDescription(){ return description; }

    /**
     * Modifie la probabilité de lâcher du butin.
     * @param chance valeur entre 0.0 (jamais) et 1.0 (toujours)
     */
    public void setChanceButin(float chance) { this.chanceButin = chance; }
}