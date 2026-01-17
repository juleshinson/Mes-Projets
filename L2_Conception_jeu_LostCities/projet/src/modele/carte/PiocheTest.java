package modele.carte;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PiocheTest {

    @Test
    public void testPioche(){
        Carte c1 = new Carte(Couleur.ROUGE, TypeCarte.PARI);
        Carte c2 = new Carte(Couleur.BLEU, TypeCarte.QUATRE);
        Carte c3 = new Carte(Couleur.BLANC, TypeCarte.DEUX);
        Carte c4 = new Carte(Couleur.JAUNE, TypeCarte.HUIT);
        Pioche p = new Pioche();
        p.init(c1,c2,c3,c4);
        int taille = p.getNbCartes();
        for(int i = 0; i < taille; i++){
            Carte c = p.prendreCarte();
        }
        for(Carte i : p){
            System.out.println(i);
        }
        Carte c = p.prendreCarte();
        System.out.println(c);
        assertTrue(p.estVide());
    }
}
