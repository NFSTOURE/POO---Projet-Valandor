package com.valandor.command;

import com.valandor.item.Objet;

/**
 * Commande pour examiner un objet en detail.
 * Recherche d'abord dans l'inventaire du joueur,
 * puis dans les objets au sol du lieu actuel.
 *
 * Exemple d'utilisation : "regarder epee", "examiner casque"
 */
public class CommandeRegard implements Commande {

    /** Nom de l'objet a examiner */
    private String nomObjet;

    /**
     * @param nomObjet nom ou partie du nom de l'objet a examiner
     */
    public CommandeRegard(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    /**
     * Cherche l'objet dans l'inventaire en premier,
     * puis dans le lieu si non trouve dans l'inventaire.
     * Affiche les details complets de l'objet (toString()).
     */
    @Override
    public void executer(ContexteJeu contexte) {
        // Cherche d'abord dans l'inventaire
        Objet objet = contexte.getJoueur()
                .getInventaire().trouverParNom(nomObjet);

        // Si pas trouve, cherche au sol dans le lieu
        if (objet == null) {
            objet = contexte.getLieuCourant()
                    .trouverObjetParNom(nomObjet);
        }

        if (objet == null) {
            System.out.println("  Impossible d'examiner : " + nomObjet);
            return;
        }
        // Affiche les details complets via toString()
        System.out.println("  " + objet.toString());
    }
}