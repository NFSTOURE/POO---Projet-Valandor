package com.valandor.system;

import com.valandor.command.*;

/**
 * Analyse les entrées texte du joueur et les convertit en objets Commande.
 * Implémente le patron de conception Commande (Command Pattern) :
 * chaque action possible est encapsulée dans une classe dédiée.
 *
 * Fonctionnement :
 * 1. L'entrée est normalisée (minuscules, espaces supprimés)
 * 2. Le premier mot (token[0]) identifie l'action
 * 3. Les mots suivants sont les paramètres de la commande
 * 4. La commande concrète correspondante est instanciée et retournée
 *
 * Avantage du patron Command : ajouter une nouvelle commande
 * ne nécessite que la création d'une nouvelle classe et l'ajout
 * d'un case dans le switch — aucune modification du code existant
 * (principe Ouvert/Fermé).
 */
public class AnalyseurCommandes {

    /**
     * Analyse une entrée texte et retourne la commande correspondante.
     * Supporte les alias (ex: "n" pour "nord", "i" pour "inventaire").
     * Gère les erreurs de saisie avec des messages explicites.
     *
     * @param entree la chaîne saisie par le joueur
     * @return la commande à exécuter, ou null si entrée invalide
     */
    public Commande analyser(String entree) {
        if (entree == null || entree.isBlank()) return null;

        // Normalisation : minuscules + découpage par espaces
        String[] tokens = entree.trim().toLowerCase().split("\\s+");
        String   action = tokens[0];

        return switch (action) {

            // ── Déplacement ───────────────────────────────────
            case "aller", "a" -> {
                if (tokens.length < 2) {
                    System.out.println("  Aller ou ? (nord, sud, est, ouest)");
                    yield null;
                }
                yield new CommandeAller(tokens[1]);
            }
            // Alias courts pour les directions
            case "nord", "n"   -> new CommandeAller("nord");
            case "sud",  "s"   -> new CommandeAller("sud");
            case "est",  "e"   -> new CommandeAller("est");
            case "ouest","o"   -> new CommandeAller("ouest");

            // ── Objets ────────────────────────────────────────
            case "prendre", "p" -> {
                if (tokens.length < 2) {
                    System.out.println("  Prendre quoi ?");
                    yield null;
                }
                yield new CommandePrendre(tokens[1]);
            }
            case "utiliser", "u" -> {
                if (tokens.length < 2) {
                    System.out.println("  Utiliser quoi ?");
                    yield null;
                }
                yield new CommandeUtiliser(tokens[1]);
            }
            case "equiper", "eq" -> {
                if (tokens.length < 2) {
                    System.out.println("  Equiper quoi ?");
                    yield null;
                }
                yield new CommandeEquiper(tokens[1]);
            }
            case "desequiper", "deseq" -> {
                if (tokens.length < 2) {
                    System.out.println(
                        "  Desequiper quel emplacement ? (arme, armure, casque)");
                    yield null;
                }
                yield new CommandeDesequiper(tokens[1]);
            }
            case "regarder", "r", "examiner" -> {
                if (tokens.length < 2) {
                    // Sans argument : décrit le lieu actuel
                    yield new CommandeRegarderLieu();
                }
                // Avec argument : examine un objet spécifique
                yield new CommandeRegard(tokens[1]);
            }

            // ── Combat ────────────────────────────────────────
            case "attaquer", "attaque", "att" -> {
                if (tokens.length < 2) {
                    System.out.println("  Attaquer qui ?");
                    yield null;
                }
                yield new CommandeAttaquer(tokens[1]);
            }

            // ── Interactions ──────────────────────────────────
            case "parler", "parle" -> {
                if (tokens.length < 2) {
                    System.out.println("  Parler a qui ?");
                    yield null;
                }
                // tokens[2] = sujet optionnel, "bonjour" par défaut
                String sujet = tokens.length >= 3 ? tokens[2] : "bonjour";
                yield new CommandeParler(tokens[1], sujet);
            }

            // ── Informations ──────────────────────────────────
            case "inventaire", "i"    -> new CommandeInventaire();
            case "statut",     "st"   -> new CommandeStatut();
            case "quetes",     "q",
                 "journal"            -> new CommandeQuetes();
            case "sauvegarder","save" -> new CommandeSauvegarder();
            case "aide",       "?"    -> new CommandeAide();

            // ── Commande inconnue ─────────────────────────────
            default -> {
                System.out.println(
                    "  Commande inconnue : " + action + " (tapez 'aide')");
                yield null;
            }
        };
    }
}