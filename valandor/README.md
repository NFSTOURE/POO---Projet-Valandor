# Les Chroniques de Valandor
### RPG Textuel en Java — Projet POO

---

## Description

Les Chroniques de Valandor est un jeu de rôle textuel développé en Java
dans le cadre du cours de Programmation Orientée Objet.
Le joueur incarne un aventurier qui explore un monde médiéval-fantastique,
combat des ennemis, gère son équipement et accomplit des quêtes.

---

## Prérequis

- Java 17+
- Maven 3.6+

Vérification :
java --version
mvn --version

---

## Installation et lancement

### 1. Cloner ou télécharger le projet

### 2. Compiler le projet
mvn compile
### 3. Lancer le jeu
mvn exec:java "-Dexec.mainClass=com.valandor.Main"

### 4. Lancer les tests
mvn test

---

## Commandes du jeu

### Déplacement
| Commande | Description |
|---|---|
| `nord` / `n` | Aller vers le nord |
| `sud` / `s` | Aller vers le sud |
| `est` / `e` | Aller vers l'est |
| `ouest` / `o` | Aller vers l'ouest |
| `aller [direction]` | Aller dans une direction |

### Objets
| Commande | Description |
|---|---|
| `prendre [objet]` | Ramasser un objet au sol |
| `utiliser [objet]` | Utiliser un objet de l'inventaire |
| `equiper [objet]` | Équiper un objet |
| `desequiper [emplacement]` | Déséquiper arme / armure / casque |
| `regarder [objet]` | Examiner un objet |
| `regarder` | Décrire le lieu actuel |

### Combat
| Commande | Description |
|---|---|
| `attaquer [ennemi]` | Attaquer un ennemi présent |

### Interactions
| Commande | Description |
|---|---|
| `parler [pnj]` | Parler à un PNJ |
| `parler [pnj] [sujet]` | Parler d'un sujet précis |

### Informations
| Commande | Description |
|---|---|
| `inventaire` / `i` | Afficher l'inventaire |
| `statut` / `st` | Afficher les stats du personnage |
| `quetes` / `q` | Afficher le journal de quêtes |
| `aide` / `?` | Afficher toutes les commandes |

### Sauvegarde
| Commande | Description |
|---|---|
| `sauvegarder` | Sauvegarder la partie en JSON |
| `charger` | Charger une partie sauvegardée |
| `quitter` | Quitter le jeu |

---

## Carte du monde
[Ruines Anciennes]
|
nord/sud
|
[Forêt Sombre] ── est/ouest ── [Caverne des Gobelins]
|
nord/sud
|
[Village de Valandor] ── est/ouest ── [Marché]

---

## Objets disponibles

| Objet | Type | Lieu | Effet |
|---|---|---|---|
| Épée rouillée | Arme (1D6+2) | Village | +2 dégâts |
| Épée longue | Arme (2D6+5) | Marché | +5 dégâts |
| Épée ancienne | Arme (2D8+8) | Ruines | +8 dégâts magiques |
| Casque de fer | Armure | Village | +3 DEF |
| Armure de cuir | Armure | Marché | +5 DEF |
| Potion de soin | Potion | Village | +30 PV |
| Potion de force | Potion | Marché | +5 ATT |
| Herbes médicinales | Potion | Forêt | +20 PV |

---

## Ennemis

| Ennemi | PV | ATT | DEF | XP |
|---|---|---|---|---|
| Gobelin | 30 | 8 | 2 | 50 |
| Chef Gobelin | 60 | 12 | 4 | 120 |
| Squelette | 40 | 10 | 6 | 80 |
| Gobelin voleur | 25 | 7 | 1 | 40 |

---

## Quêtes

### Menace Gobeline
- **Donneur** : Forgeron (Village)
- **Objectif** : Tuer 2 Gobelins dans la Forêt
- **Récompense** : 150 XP + Grande Potion de soin

### Secret des Ruines
- **Donneur** : Vieux Sage (Village)
- **Objectif** : Vaincre le Squelette dans les Ruines
- **Récompense** : 200 XP + Potion de défense

---

## Système de combat

1. Lancer D20 pour toucher (résultat >= 5 = succès)
2. Si touché : lancer les dés de l'arme + bonus ATT
3. Dégâts réels = dégâts bruts - DEF de l'ennemi
4. Tour de l'ennemi : attaque automatique
5. Dégâts reçus = ATT ennemi - DEF joueur

---

## Progression du personnage

| Niveau | XP nécessaire | PV max | ATT | DEF |
|---|---|---|---|---|
| 1 | 100 XP | 100 | 10 | 5 |
| 2 | 200 XP | 120 | 13 | 7 |
| 3 | 300 XP | 140 | 16 | 9 |

Formule : niveau N → N+1 requiert N × 100 XP

---

## Architecture du projet
src/main/java/com/valandor/
├── entity/         EntiteVivante, Joueur, Ennemi, PNJ
├── item/           Objet, Arme, Armure, Consommable, Potion
├── world/          Lieu, Quete
├── command/        Commande (interface) + 12 commandes concrètes
├── system/         Inventaire, Des, AnalyseurCommandes, GestionSauvegarde
└── Main.java

---

## Concepts POO illustrés

- **Héritage** : EntiteVivante → Joueur / Ennemi / PNJ
- **Abstraction** : classes abstraites EntiteVivante et Objet
- **Composition** : Joueur possède un Inventaire
- **Patron Command** : chaque action encapsulée dans une classe
- **Polymorphisme** : utiliser() et agir() surchargés par chaque sous-classe
- **Sérialisation** : sauvegarde JSON via Gson

---

## Auteur

Ndeye Fatou Sène TOURE  : Projet réalisé dans le cadre du cours POO — M2 GPI