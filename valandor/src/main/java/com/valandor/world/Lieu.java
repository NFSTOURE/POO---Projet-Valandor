package com.valandor.world;

import com.valandor.entity.EntiteVivante;
import com.valandor.item.Objet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un lieu du monde de Valandor.
 * Un lieu possède des sorties vers d'autres lieux,
 * des objets au sol et des entités vivantes (PNJ, ennemis).
 */
public class Lieu {

    // ── Attributs ──────────────────────────────────────────
    private String              nom;
    private String              description;
    private Map<String, Lieu>   sorties;  // clé = direction (nord/sud/est/ouest)
    private List<Objet>         objets;   // objets ramassables au sol
    private List<EntiteVivante> entites;  // PNJ et ennemis présents

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un nouveau lieu avec un nom et une description.
     * @param nom         nom affiché au joueur
     * @param description texte descriptif affiché à l'arrivée
     */
    public Lieu(String nom, String description) {
        this.nom         = nom;
        this.description = description;
        this.sorties     = new HashMap<>();
        this.objets      = new ArrayList<>();
        this.entites     = new ArrayList<>();
    }

    // ── Gestion des sorties ────────────────────────────────

    /**
     * Ajoute une sortie vers un autre lieu dans une direction donnée.
     * @param direction "nord", "sud", "est" ou "ouest"
     * @param lieu      le lieu destination
     */
    public void ajouterSortie(String direction, Lieu lieu) {
        sorties.put(direction.toLowerCase(), lieu);
    }

    /**
     * Retourne le lieu dans la direction indiquée, ou null si aucune sortie.
     */
    public Lieu getSortie(String direction) {
        return sorties.get(direction.toLowerCase());
    }

    /**
     * Vérifie si une sortie existe dans la direction donnée.
     */
    public boolean aSortie(String direction) {
        return sorties.containsKey(direction.toLowerCase());
    }

    // ── Gestion des objets ─────────────────────────────────

    /**
     * Ajoute un objet au sol dans ce lieu.
     */
    public void ajouterObjet(Objet objet) {
        objets.add(objet);
    }

    /**
     * Retire un objet du sol (quand le joueur le ramasse).
     * @return true si l'objet a été trouvé et retiré
     */
    public boolean retirerObjet(Objet objet) {
        return objets.remove(objet);
    }

    /**
     * Recherche un objet par son nom (insensible à la casse et aux accents).
     * Utilise contains() pour permettre une recherche partielle.
     * Ex: "epee" trouve "Épée rouillée"
     * @return l'objet trouvé ou null
     */
    public Objet trouverObjetParNom(String nom) {
        String nomNormalise = normaliser(nom);
        return objets.stream()
                .filter(o -> normaliser(o.getNom()).contains(nomNormalise))
                .findFirst()
                .orElse(null);
    }

    // ── Gestion des entités ────────────────────────────────

    /**
     * Ajoute une entité vivante (ennemi ou PNJ) dans ce lieu.
     */
    public void ajouterEntite(EntiteVivante entite) {
        entites.add(entite);
    }

    /**
     * Retire une entité du lieu (ex: ennemi vaincu).
     * @return true si l'entité a été trouvée et retirée
     */
    public boolean retirerEntite(EntiteVivante entite) {
        return entites.remove(entite);
    }

    /**
     * Recherche une entité par son nom (insensible à la casse et aux accents).
     * Utilise contains() pour permettre une recherche partielle.
     * Ex: "sage" trouve "Vieux Sage"
     * @return l'entité trouvée ou null
     */
    public EntiteVivante trouverEntiteParNom(String nom) {
        String nomNormalise = normaliser(nom);
        return entites.stream()
                .filter(e -> normaliser(e.getNom()).contains(nomNormalise))
                .findFirst()
                .orElse(null);
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Affiche la description complète du lieu :
     * nom, description, sorties, objets au sol, entités présentes.
     */
    public void decrire() {
        System.out.println("\n══════════════════════════════");
        System.out.println("  " + nom);
        System.out.println("══════════════════════════════");
        System.out.println("  " + description);

        // Affiche les sorties disponibles
        if (!sorties.isEmpty()) {
            System.out.print("  Sorties : ");
            System.out.println(String.join(", ", sorties.keySet()));
        }

        // Affiche les objets ramassables
        if (!objets.isEmpty()) {
            System.out.println("  Objets ici :");
            objets.forEach(o -> System.out.println("    - " + o.getNom()));
        }

        // Affiche uniquement les entités encore vivantes
        entites.stream()
                .filter(EntiteVivante::estVivant)
                .forEach(e -> System.out.println("  * " + e.getNom() + " est ici."));
    }

    // ── Utilitaire ─────────────────────────────────────────

    /**
     * Normalise un texte pour la recherche :
     * - conversion en minuscules
     * - suppression des accents (é→e, à→a, etc.)
     * Permet de taper "epee" pour trouver "Épée rouillée".
     */
    private String normaliser(String texte) {
        return java.text.Normalizer
                .normalize(texte.toLowerCase(), java.text.Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    // ── Getters ────────────────────────────────────────────
    public String               getNom()        { return nom; }
    public String               getDescription(){ return description; }
    public Map<String, Lieu>    getSorties()    { return sorties; }
    public List<Objet>          getObjets()     { return objets; }
    public List<EntiteVivante>  getEntites()    { return entites; }
}