package modele.strategie;

import modele.Coup;
import modele.Jeu;
import modele.Joueur;
import modele.TypeCoup;
import modele.carte.*;

public class BotMinAmeliore implements Strategie {

    @Override
    public Coup choisirCoup(Jeu jeu) {

        Joueur j = jeu.getJoueurCourant();
        Main main = j.getMain();

        //Chercher une carte jouable avec la plus petite valeur possible
        int indiceJouable = -1;
        int valeurMin = Integer.MAX_VALUE;

        for (int i = 0; i < main.getNbCartes(); i++) {
            Carte c = main.voirCarte(i);

            if (j.getExpedition(c.getCouleur()).peutPoser(c)) {
                int v = c.getType().getValeur();
                if (v < valeurMin) {
                    valeurMin = v;
                    indiceJouable = i;
                }
            }
        }

        if (indiceJouable != -1) {
            return new Coup(indiceJouable, TypeCoup.JOUER);
        }

        //Sinon : aucune carte jouable, défausse "mieux que hasard"
        // On préfère défausser une carte qui ne sert pas tout de suite :
        // - une carte chiffrée haute
        // - ou un PARI (valeur 0) si on ne peut pas démarrer l'expédition
        int indiceDefausse = -1;
        int meilleurScore = -1;

        for (int i = 0; i < main.getNbCartes(); i++) {
            Carte c = main.voirCarte(i);

            // Plus le score est grand, plus on a envie de défausser cette carte
            int score = 0;

            // Les grosses valeurs sont difficiles à jouer tôt
            score += c.getType().getValeur();

            // Si c'est un PARI, ça peut être dangereux à garder si on ne peut pas construire derrière
            if (c.getType() == TypeCarte.PARI) {
                score += 5;
            }

            // on augmente la priorité de défausse
            if (!j.getExpedition(c.getCouleur()).peutPoser(c)) {
                score += 10;
            }

            if (score > meilleurScore) {
                meilleurScore = score;
                indiceDefausse = i;
            }
        }

        if (indiceDefausse == -1) {
            indiceDefausse = 0;
        }

        return new Coup(indiceDefausse, TypeCoup.DEFAUSSER);
    }

    @Override
    public Couleur choisirPioche(Jeu jeu) {

        Joueur j = jeu.getJoueurCourant();

        // Stratégie simple :
        // Si une défausse a une carte au sommet qui est jouable immédiatement,
        // on la prend. Sinon on pioche dans la pioche principale.
        for (Couleur c : Couleur.values()) {
            Defausse d = jeu.getDefausse(c);
            if (d != null && !d.estVide()) {
                Carte top = d.voirDernierCarte();
                if (top != null && j.getExpedition(top.getCouleur()).peutPoser(top)) {
                    return c;
                }
            }
        }

        return null; // pioche principale
    }
}


