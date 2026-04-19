package com.valandor;

import com.valandor.item.Potion;
import com.valandor.item.Arme;
import com.valandor.item.Objet;
import com.valandor.system.Inventaire;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class InventaireTest {

    private Inventaire inventaire;
    private Arme       epee;
    private Potion     potion;

    @BeforeEach
    void setUp() {
        inventaire = new Inventaire(10.0f);
        epee       = new Arme("Epee", "Une epee.", 3.0f, 2, 1, 6, "tranchant");
        potion     = new Potion("Potion", 30, "soin");
    }

    @Test
    void testAjouterObjet() {
        assertTrue(inventaire.ajouterObjet(epee));
        assertTrue(inventaire.possede("Epee"));
    }

    @Test
    void testRetirerObjet() {
        inventaire.ajouterObjet(epee);
        inventaire.retirerObjet(epee);
        assertFalse(inventaire.possede("Epee"));
    }

    @Test
    void testLimitePoids() {
        Arme lourde = new Arme("Epee lourde", "Tres lourde.", 8.0f, 5, 2, 6, "tranchant");
        inventaire.ajouterObjet(lourde);
        // La potion depasse le poids max restant
        assertFalse(inventaire.ajouterObjet(
                new Arme("Epee2", "Epee.", 5.0f, 2, 1, 6, "tranchant")));
    }

    @Test
    void testTrouverParNom() {
        inventaire.ajouterObjet(potion);
        assertNotNull(inventaire.trouverParNom("Potion"));
        assertNull(inventaire.trouverParNom("Objet inexistant"));
    }

    @Test
    void testPoidsCalculeCorrectement() {
        inventaire.ajouterObjet(epee);
        inventaire.ajouterObjet(potion);
        assertEquals(3.0f + 0.5f, inventaire.getPoidsCourant(), 0.01f);
    }

    @Test
    void testInventaireVide() {
        assertFalse(inventaire.possede("nimporte quoi"));
        assertEquals(0.0f, inventaire.getPoidsCourant(), 0.01f);
    }
}