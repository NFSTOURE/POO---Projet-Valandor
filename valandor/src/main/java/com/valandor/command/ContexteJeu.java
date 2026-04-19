package com.valandor.command;

import com.valandor.entity.Joueur;
import com.valandor.world.Lieu;
import java.util.HashMap;
import java.util.Map;

public class ContexteJeu {

    // ── Attributs ──────────────────────────────────────────
    private Joueur            joueur;
    private Lieu              lieuCourant;
    private Map<String, Lieu> monde;
    private boolean           enCours;

    // ── Constructeur ───────────────────────────────────────
    public ContexteJeu(Joueur joueur, Lieu lieuDepart) {
        this.joueur      = joueur;
        this.lieuCourant = lieuDepart;
        this.monde       = new HashMap<>();
        this.enCours     = true;
    }

    // ── Getters & Setters ──────────────────────────────────
    public Joueur  getJoueur()                     { return joueur; }
    public Lieu    getLieuCourant()                { return lieuCourant; }
    public void    setLieuCourant(Lieu lieu)       { this.lieuCourant = lieu; }
    public boolean isEnCours()                     { return enCours; }
    public void    terminer()                      { this.enCours = false; }
    public void    ajouterLieu(String id, Lieu lieu){ monde.put(id, lieu); }
    public Lieu    getLieu(String id)              { return monde.get(id); }
}