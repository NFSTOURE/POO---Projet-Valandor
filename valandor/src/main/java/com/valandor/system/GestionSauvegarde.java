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

public class GestionSauvegarde {

    private static final String DOSSIER_SAVES = "saves/";
    private static final Gson   gson = new GsonBuilder().setPrettyPrinting().create();

    // ── Sauvegarde ─────────────────────────────────────────

    public static void sauvegarder(Joueur joueur, String nomFichier) {
        JsonObject sauvegarde = new JsonObject();

        // Infos du joueur
        sauvegarde.addProperty("nom",         joueur.getNom());
        sauvegarde.addProperty("pvMax",        joueur.getPvMax());
        sauvegarde.addProperty("pvCourants",   joueur.getPvCourants());
        sauvegarde.addProperty("attaque",      joueur.getAttaque());
        sauvegarde.addProperty("defense",      joueur.getDefense());
        sauvegarde.addProperty("xp",           joueur.getXp());
        sauvegarde.addProperty("niveau",       joueur.getNiveau());
        sauvegarde.addProperty("lieuActuel",   joueur.getLieuActuel());

        // Inventaire
        com.google.gson.JsonArray inventaireJson = new com.google.gson.JsonArray();
        for (Objet objet : joueur.getInventaire().getObjets()) {
            JsonObject objetJson = new JsonObject();
            objetJson.addProperty("nom",         objet.getNom());
            objetJson.addProperty("description", objet.getDescription());
            objetJson.addProperty("poids",       objet.getPoids());
            objetJson.addProperty("valeur",      objet.getValeur());
            objetJson.addProperty("type",        objet.getClass().getSimpleName());
            inventaireJson.add(objetJson);
        }
        sauvegarde.add("inventaire", inventaireJson);

        // Equipement
        com.google.gson.JsonArray equipementJson = new com.google.gson.JsonArray();
        joueur.getEquipement().forEach((emplacement, objet) -> {
            JsonObject slotJson = new JsonObject();
            slotJson.addProperty("emplacement",  emplacement);
            slotJson.addProperty("nom",          objet.getNom());
            slotJson.addProperty("type",         objet.getClass().getSimpleName());
            equipementJson.add(slotJson);
        });
        sauvegarde.add("equipement", equipementJson);

        // Ecriture du fichier
        try {
            Files.createDirectories(Paths.get(DOSSIER_SAVES));
            String chemin = DOSSIER_SAVES + nomFichier + ".json";
            Files.writeString(Paths.get(chemin), gson.toJson(sauvegarde));
            System.out.println("  Partie sauvegardee : " + chemin);
        } catch (IOException e) {
            System.out.println("  Erreur de sauvegarde : " + e.getMessage());
        }
    }

    // ── Chargement ─────────────────────────────────────────

    public static Joueur charger(String nomFichier) {
        String chemin = DOSSIER_SAVES + nomFichier + ".json";
        try {
            String contenu = Files.readString(Paths.get(chemin));
            JsonObject sauvegarde = gson.fromJson(contenu, JsonObject.class);

            // Reconstruction du joueur
            String nom = sauvegarde.get("nom").getAsString();
            Joueur joueur = new Joueur(nom);

            joueur.setPvCourants(sauvegarde.get("pvCourants").getAsInt());
            joueur.setAttaque(sauvegarde.get("attaque").getAsInt());
            joueur.setDefense(sauvegarde.get("defense").getAsInt());

            // XP et niveau via reflexion simple
            restaurerNiveauEtXP(joueur,
                    sauvegarde.get("niveau").getAsInt(),
                    sauvegarde.get("xp").getAsInt());

            joueur.setLieuActuel(sauvegarde.get("lieuActuel").getAsString());

            // Reconstruction de l'inventaire
            sauvegarde.get("inventaire").getAsJsonArray().forEach(elem -> {
                JsonObject obj = elem.getAsJsonObject();
                String type    = obj.get("type").getAsString();
                String nomObj  = obj.get("nom").getAsString();
                float  poids   = obj.get("poids").getAsFloat();

                Objet objet = switch (type) {
                    case "Arme"   -> new Arme(nomObj, obj.get("description").getAsString(),
                            poids, obj.get("valeur").getAsInt(), 1, 6, "tranchant");
                    case "Potion" -> new Potion(nomObj, obj.get("valeur").getAsInt(), "soin");
                    default       -> new Potion(nomObj, 0, "soin");
                };
                joueur.getInventaire().ajouterObjet(objet);
            });

            System.out.println("  Partie chargee : " + chemin);
            System.out.println("  Bienvenue de retour, " + nom + " !");
            return joueur;

        } catch (IOException e) {
            System.out.println("  Aucune sauvegarde trouvee : " + nomFichier);
            return null;
        }
    }

    // ── Utilitaire ─────────────────────────────────────────

    private static void restaurerNiveauEtXP(Joueur joueur, int niveau, int xp) {
        // Monte le joueur au bon niveau sans effets secondaires
        try {
            var niveauField = Joueur.class.getDeclaredField("niveau");
            var xpField     = Joueur.class.getDeclaredField("xp");
            var pvMaxField  = joueur.getClass().getSuperclass().getDeclaredField("pvMax");
            niveauField.setAccessible(true);
            xpField.setAccessible(true);
            pvMaxField.setAccessible(true);
            niveauField.set(joueur, niveau);
            xpField.set(joueur, xp);
        } catch (Exception e) {
            System.out.println("  Avertissement restauration niveau : " + e.getMessage());
        }
    }

    // ── Liste des sauvegardes ──────────────────────────────

    public static void listerSauvegardes() {
        File dossier = new File(DOSSIER_SAVES);
        if (!dossier.exists() || dossier.listFiles() == null) {
            System.out.println("  Aucune sauvegarde disponible.");
            return;
        }
        System.out.println("  Sauvegardes disponibles :");
        for (File f : dossier.listFiles()) {
            if (f.getName().endsWith(".json")) {
                System.out.println("    - " + f.getName().replace(".json", ""));
            }
        }
    }
}