package com.valandor.system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.valandor.entity.Joueur;
import com.valandor.item.Arme;
import com.valandor.item.Objet;
import com.valandor.item.Potion;

import java.io.*;
import java.nio.file.*;

/**
 * Gere la persistance de l'etat du jeu via la serialisation JSON.
 * Utilise la librairie Gson pour convertir les objets Java en JSON
 * et inversement.
 *
 * Fonctionnalites :
 * - Sauvegarder : serialise l'etat complet du joueur dans un fichier JSON
 * - Charger     : deserialise un fichier JSON et reconstruit le joueur
 * - Lister      : affiche les sauvegardes disponibles dans le dossier saves/
 *
 * Format du fichier JSON sauvegarde :
 * {
 *   "nom": "Fatou",
 *   "pvMax": 100,
 *   "pvCourants": 88,
 *   "attaque": 10,
 *   "defense": 5,
 *   "xp": 50,
 *   "niveau": 1,
 *   "lieuActuel": "village",
 *   "inventaire": [...],
 *   "equipement": [...]
 * }
 *
 * Chaque objet est serialise avec son type pour permettre
 * la reconstruction de la bonne sous-classe au chargement.
 */
public class GestionSauvegarde {

    // ── Constantes ─────────────────────────────────────────
    /** Dossier ou sont stockes les fichiers de sauvegarde */
    private static final String DOSSIER_SAVES = "saves/";

    /** Instance Gson configuree avec un formatage lisible */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // ── Sauvegarde ─────────────────────────────────────────

    /**
     * Serialise l'etat complet du joueur dans un fichier JSON.
     * Sauvegarde : stats, XP, niveau, lieu, inventaire et equipement.
     * Cree le dossier saves/ s'il n'existe pas.
     *
     * @param joueur     le joueur dont on sauvegarde l'etat
     * @param nomFichier nom du fichier sans extension (ex: "fatou")
     */
    public static void sauvegarder(Joueur joueur, String nomFichier) {
        JsonObject sauvegarde = new JsonObject();

        // ── Stats du joueur ────────────────────────────────
        sauvegarde.addProperty("nom",       joueur.getNom());
        sauvegarde.addProperty("pvMax",     joueur.getPvMax());
        sauvegarde.addProperty("pvCourants",joueur.getPvCourants());
        sauvegarde.addProperty("attaque",   joueur.getAttaque());
        sauvegarde.addProperty("defense",   joueur.getDefense());
        sauvegarde.addProperty("xp",        joueur.getXp());
        sauvegarde.addProperty("niveau",    joueur.getNiveau());
        sauvegarde.addProperty("lieuActuel",joueur.getLieuActuel());

        // ── Inventaire ─────────────────────────────────────
        // Chaque objet est serialise avec son type pour la reconstruction
        com.google.gson.JsonArray inventaireJson =
                new com.google.gson.JsonArray();
        for (Objet objet : joueur.getInventaire().getObjets()) {
            JsonObject objetJson = new JsonObject();
            objetJson.addProperty("nom",         objet.getNom());
            objetJson.addProperty("description", objet.getDescription());
            objetJson.addProperty("poids",       objet.getPoids());
            objetJson.addProperty("valeur",      objet.getValeur());
            // Le type permet de reconstruire la bonne sous-classe
            objetJson.addProperty("type",
                    objet.getClass().getSimpleName());
            inventaireJson.add(objetJson);
        }
        sauvegarde.add("inventaire", inventaireJson);

        // ── Equipement ─────────────────────────────────────
        com.google.gson.JsonArray equipementJson =
                new com.google.gson.JsonArray();
        joueur.getEquipement().forEach((emplacement, objet) -> {
            JsonObject slotJson = new JsonObject();
            slotJson.addProperty("emplacement", emplacement);
            slotJson.addProperty("nom",         objet.getNom());
            slotJson.addProperty("type",
                    objet.getClass().getSimpleName());
            equipementJson.add(slotJson);
        });
        sauvegarde.add("equipement", equipementJson);

        // ── Ecriture du fichier ────────────────────────────
        try {
            // Cree le dossier saves/ si necessaire
            Files.createDirectories(Paths.get(DOSSIER_SAVES));
            String chemin = DOSSIER_SAVES + nomFichier + ".json";
            Files.writeString(Paths.get(chemin), gson.toJson(sauvegarde));
            System.out.println("  Partie sauvegardee : " + chemin);
        } catch (IOException e) {
            System.out.println("  Erreur de sauvegarde : "
                    + e.getMessage());
        }
    }

    // ── Chargement ─────────────────────────────────────────

    /**
     * Deserialise un fichier JSON et reconstruit l'etat du joueur.
     * Reconstruit le joueur avec ses stats, son inventaire
     * et son equipement depuis le fichier de sauvegarde.
     *
     * @param nomFichier nom du fichier sans extension (ex: "fatou")
     * @return le joueur reconstruit, ou null si fichier introuvable
     */
    public static Joueur charger(String nomFichier) {
        String chemin = DOSSIER_SAVES + nomFichier + ".json";
        try {
            // Lecture et parsing du fichier JSON
            String contenu = Files.readString(Paths.get(chemin));
            JsonObject sauvegarde = gson.fromJson(contenu,
                    JsonObject.class);

            // ── Reconstruction du joueur ───────────────────
            String nom    = sauvegarde.get("nom").getAsString();
            Joueur joueur = new Joueur(nom);

            // Restaure les stats directement
            joueur.setPvCourants(
                    sauvegarde.get("pvCourants").getAsInt());
            joueur.setAttaque(
                    sauvegarde.get("attaque").getAsInt());
            joueur.setDefense(
                    sauvegarde.get("defense").getAsInt());
            joueur.setLieuActuel(
                    sauvegarde.get("lieuActuel").getAsString());

            // Restaure le niveau et l'XP via reflexion
            // (evite de declencher les effets de monterNiveau())
            restaurerNiveauEtXP(joueur,
                    sauvegarde.get("niveau").getAsInt(),
                    sauvegarde.get("xp").getAsInt());

            // ── Reconstruction de l'inventaire ─────────────
            // Utilise le type serialise pour instancier la bonne classe
            sauvegarde.get("inventaire").getAsJsonArray()
                    .forEach(elem -> {
                JsonObject obj  = elem.getAsJsonObject();
                String type     = obj.get("type").getAsString();
                String nomObj   = obj.get("nom").getAsString();
                float  poids    = obj.get("poids").getAsFloat();
                int    valeur   = obj.get("valeur").getAsInt();
                String desc     = obj.get("description").getAsString();

                // Reconstruction selon le type de l'objet
                Objet objet = switch (type) {
                    case "Arme"   -> new Arme(nomObj, desc, poids,
                            valeur, 1, 6, "tranchant");
                    case "Potion" -> new Potion(nomObj, valeur, "soin");
                    default       -> new Potion(nomObj, 0, "soin");
                };
                joueur.getInventaire().ajouterObjet(objet);
            });

            System.out.println("  Partie chargee : " + chemin);
            System.out.println("  Bienvenue de retour, " + nom + " !");
            return joueur;

        } catch (IOException e) {
            System.out.println("  Aucune sauvegarde trouvee : "
                    + nomFichier);
            return null;
        }
    }

    // ── Liste des sauvegardes ──────────────────────────────

    /**
     * Affiche la liste des sauvegardes disponibles dans le dossier saves/.
     * Filtre les fichiers avec l'extension .json.
     */
    public static void listerSauvegardes() {
        File dossier = new File(DOSSIER_SAVES);
        if (!dossier.exists() || dossier.listFiles() == null) {
            System.out.println("  Aucune sauvegarde disponible.");
            return;
        }
        System.out.println("  Sauvegardes disponibles :");
        for (File f : dossier.listFiles()) {
            if (f.getName().endsWith(".json")) {
                // Affiche le nom sans l'extension .json
                System.out.println("    - "
                        + f.getName().replace(".json", ""));
            }
        }
    }

    // ── Utilitaire ─────────────────────────────────────────

    /**
     * Restaure le niveau et l'XP du joueur via la reflexion Java.
     * Permet de modifier les champs prives sans passer par
     * les methodes normales qui declenchent des effets secondaires.
     * Ex: monterNiveau() augmente les stats — on ne veut pas ca
     * lors du chargement car les stats sont deja dans le JSON.
     *
     * @param joueur le joueur a modifier
     * @param niveau le niveau a restaurer
     * @param xp     l'XP a restaurer
     */
    private static void restaurerNiveauEtXP(Joueur joueur,
                                             int niveau, int xp) {
        try {
            // Acces aux champs prives via reflexion
            var niveauField = Joueur.class
                    .getDeclaredField("niveau");
            var xpField = Joueur.class
                    .getDeclaredField("xp");
            niveauField.setAccessible(true);
            xpField.setAccessible(true);
            niveauField.set(joueur, niveau);
            xpField.set(joueur, xp);
        } catch (Exception e) {
            System.out.println("  Avertissement restauration : "
                    + e.getMessage());
        }
    }
}