package com.valandor.system;

import com.valandor.item.Objet;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère l'inventaire du joueur par composition.
 * Le joueur "possède" un inventaire (relation de composition POO).
 * L'inventaire contrôle la limite de poids et expose
 * les opérations CRUD sur les objets portés.
 *
 * Relation POO : Inventaire est utilisé par composition dans Joueur
 * (le joueur ne "est pas" un inventaire, il "possède" un inventaire)
 */
public class Inventaire {

    // ── Attributs ──────────────────────────────────────────
    private List<Objet> objets;        // liste des objets portés
    private float       poidsMax;      // capacité maximale en kg
    private float       poidsCourant;  // poids total actuel en kg

    // ── Constructeur ───────────────────────────────────────

    /**
     * Crée un inventaire vide avec une capacité maximale donnée.
     *
     * @param poidsMax capacité maximale de l'inventaire en kg
     */
    public Inventaire(float poidsMax) {
        this.objets       = new ArrayList<>();
        this.poidsMax     = poidsMax;
        this.poidsCourant = 0;
    }

    // ── Opérations CRUD ────────────────────────────────────

    /**
     * Ajoute un objet à l'inventaire si le poids le permet.
     * Vérifie que poidsCourant + poids objet <= poidsMax.
     *
     * @param objet l'objet à ajouter
     * @return true si l'ajout a réussi, false si l'inventaire est plein
     */
    public boolean ajouterObjet(Objet objet) {
        // Vérifie la limite de poids avant d'ajouter
        if (poidsCourant + objet.getPoids() > poidsMax) {
            System.out.println("  Inventaire plein ! Impossible d'ajouter : "
                    + objet.getNom());
            return false;
        }
        objets.add(objet);
        poidsCourant += objet.getPoids();
        System.out.println("  + " + objet.getNom() + " ajouté à l'inventaire.");
        return true;
    }

    /**
     * Retire un objet de l'inventaire et met à jour le poids courant.
     *
     * @param objet l'objet à retirer
     * @return true si l'objet a été trouvé et retiré
     */
    public boolean retirerObjet(Objet objet) {
        if (objets.remove(objet)) {
            poidsCourant -= objet.getPoids();
            System.out.println("  - " + objet.getNom()
                    + " retiré de l'inventaire.");
            return true;
        }
        System.out.println("  Objet introuvable : " + objet.getNom());
        return false;
    }

    /**
     * Recherche un objet par son nom.
     * Insensible à la casse et aux accents grâce à la normalisation.
     * Utilise contains() pour une recherche partielle.
     * Ex: "epee" trouve "Épée rouillée"
     *
     * @param nom nom ou partie du nom de l'objet recherché
     * @return l'objet trouvé ou null si absent
     */
    public Objet trouverParNom(String nom) {
        String nomNormalise = normaliser(nom);
        return objets.stream()
                .filter(o -> normaliser(o.getNom()).contains(nomNormalise))
                .findFirst()
                .orElse(null);
    }

    /**
     * Vérifie si l'inventaire contient un objet avec ce nom.
     *
     * @param nom nom de l'objet recherché
     * @return true si l'objet est présent
     */
    public boolean possede(String nom) {
        return trouverParNom(nom) != null;
    }

    // ── Affichage ──────────────────────────────────────────

    /**
     * Affiche le contenu de l'inventaire avec le poids utilisé.
     * Format : Nom de l'objet — Poids kg
     */
    public void afficher() {
        if (objets.isEmpty()) {
            System.out.println("  Inventaire vide.");
            return;
        }
        System.out.printf("  Inventaire (%.1f/%.1f kg) :%n",
                poidsCourant, poidsMax);
        for (Objet o : objets) {
            System.out.printf("    - %-20s %.1f kg%n",
                    o.getNom(), o.getPoids());
        }
    }

    // ── Utilitaire ─────────────────────────────────────────

    /**
     * Normalise un texte pour la recherche :
     * conversion en minuscules + suppression des accents.
     * Permet de taper "epee" pour trouver "Épée rouillée".
     */
    private String normaliser(String texte) {
        return java.text.Normalizer
                .normalize(texte.toLowerCase(), java.text.Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    // ── Getters ────────────────────────────────────────────

    /** @return la liste des objets dans l'inventaire */
    public List<Objet> getObjets()     { return objets; }

    /** @return la capacité maximale en kg */
    public float getPoidsMax()         { return poidsMax; }

    /** @return le poids total actuel en kg */
    public float getPoidsCourant()     { return poidsCourant; }
}