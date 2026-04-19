package com.valandor;

import com.valandor.entity.Ennemi;
import com.valandor.entity.Joueur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CombatTest {

    private Joueur joueur;
    private Ennemi gobelin;

    @BeforeEach
    void setUp() {
        joueur  = new Joueur("TestJoueur");
        gobelin = new Ennemi("Gobelin", 30, 8, 2, 50, "Un gobelin.");
    }

    @Test
    void testJoueurCommenceAvecPVMax() {
        assertEquals(100, joueur.getPvCourants());
        assertEquals(100, joueur.getPvMax());
    }

    @Test
    void testSubirDegatsReduitPV() {
        gobelin.subirDegats(10);
        assertEquals(22, gobelin.getPvCourants()); // 30 - (10-2 defense) = 22
    }

    @Test
    void testDefenseAbsorbeDegats() {
        int pvAvant = joueur.getPvCourants();
        joueur.subirDegats(3); // 3 - 5 defense = 0 degats reels
        assertEquals(pvAvant, joueur.getPvCourants(),
                "La defense doit absorber tous les degats");
    }

    @Test
    void testEnnemiMortApresDegatsSuffisants() {
        gobelin.subirDegats(100);
        assertFalse(gobelin.estVivant());
        assertEquals(0, gobelin.getPvCourants());
    }

    @Test
    void testSoinLimiteAuPVMax() {
        joueur.subirDegats(20);
        joueur.soigner(1000); // soigne plus que le max
        assertEquals(joueur.getPvMax(), joueur.getPvCourants(),
                "Les PV ne doivent pas depasser le maximum");
    }

    @Test
    void testGagnerXPMonteeDeNiveau() {
        joueur.gagnerXP(100); // seuil niveau 1→2
        assertEquals(2, joueur.getNiveau());
    }

    @Test
    void testRecompenseXPEnnemi() {
        assertEquals(50, gobelin.getRecompenseXP());
    }

    @Test
    void testPVNeDevientPasNegatif() {
        gobelin.subirDegats(10000);
        assertEquals(0, gobelin.getPvCourants());
        assertFalse(gobelin.estVivant());
    }
}