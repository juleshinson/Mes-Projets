package modele.carte;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class DefausseTest {
    @Test
    public void testDefausse() {
        Carte c1 = new Carte(Couleur.ROUGE, TypeCarte.PARI);
        Carte pari = new Carte(Couleur.ROUGE, TypeCarte.PARI);
        Carte deux = new Carte(Couleur.ROUGE, TypeCarte.DEUX);
        Carte cinq = new Carte(Couleur.ROUGE, TypeCarte.CINQ);
        Carte c2 = new Carte(Couleur.BLEU, TypeCarte.QUATRE);
        Carte c3 = new Carte(Couleur.BLANC, TypeCarte.DEUX);
        Carte c4 = new Carte(Couleur.JAUNE, TypeCarte.HUIT);
        Defausse exp = new Defausse(Couleur.BLANC);

        assertTrue(exp.poser(c3));
        //assertTrue(exp.poser(c2));
        //assertTrue(exp.poser(c4));
        assertEquals(1, exp.getNbCartes());
    }
}
