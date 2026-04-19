package com.valandor;

import com.valandor.system.Des;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DesTest {

    private Des des;

    @BeforeEach
    void setUp() {
        des = new Des(42); // seed fixe = résultats prévisibles
    }

    @Test
    void testLancerD6EntreUnEtSix() {
        for (int i = 0; i < 100; i++) {
            int resultat = des.lancerD6();
            assertTrue(resultat >= 1 && resultat <= 6,
                    "D6 doit etre entre 1 et 6, obtenu : " + resultat);
        }
    }

    @Test
    void testLancerD20EntreUnEtVingt() {
        for (int i = 0; i < 100; i++) {
            int resultat = des.lancerD20();
            assertTrue(resultat >= 1 && resultat <= 20,
                    "D20 doit etre entre 1 et 20, obtenu : " + resultat);
        }
    }

    @Test
    void testLancerMultipleSommeCorrecte() {
        Des desFixe = new Des(42);
        int resultat = desFixe.lancerMultiple(3, 6);
        assertTrue(resultat >= 3 && resultat <= 18,
                "3D6 doit etre entre 3 et 18, obtenu : " + resultat);
    }

    @Test
    void testLancerFacesInvalidesLanceException() {
        assertThrows(IllegalArgumentException.class, () -> des.lancer(1));
        assertThrows(IllegalArgumentException.class, () -> des.lancer(0));
    }

    @Test
    void testLancerAvecSeedReproductible() {
        Des des1 = new Des(123);
        Des des2 = new Des(123);
        assertEquals(des1.lancerD20(), des2.lancerD20(),
                "Meme seed doit donner meme resultat");
    }
}