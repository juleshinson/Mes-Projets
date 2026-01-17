package modele.carte;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpeditionTest {

    @Test
    public void testExpeditionOrdreValide() {
        Expedition exp = new Expedition(Couleur.ROUGE);
        Carte pari = new Carte(Couleur.ROUGE, TypeCarte.PARI);
        Carte deux = new Carte(Couleur.ROUGE, TypeCarte.DEUX);
        Carte cinq = new Carte(Couleur.ROUGE, TypeCarte.CINQ);
        assertTrue(exp.poser(pari));
        assertTrue(exp.poser(deux));
        assertTrue(exp.poser(cinq));
        assertEquals(3, exp.getNbCartes());
    }
    @Test
    public void testExpeditionScoreSimple() {
        Expedition exp = new Expedition(Couleur.BLEU);
        exp.poser(new Carte(Couleur.BLEU, TypeCarte.CINQ));
        exp.poser(new Carte(Couleur.BLEU, TypeCarte.DIX));
        // (5 + 10 - 20) * 1 = -5
        assertEquals(-5, exp.calculerScore());
    }
}
