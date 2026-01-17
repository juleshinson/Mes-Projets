package modele.strategie;

import modele.Coup;
import modele.Jeu;
import modele.TypeCoup;
import modele.carte.Carte;
import modele.carte.Couleur;

// La classe BotMin représente une stratégie un peu plus intelligente.
// Ce bot essaie de jouer la plus petite carte possible s'il peut la poser.
// S'il ne peut jouer aucune carte, il défausse simplement la première carte.
public class BotMin implements Strategie {

    // Méthode qui permet au bot de choisir un coup à jouer
    @Override
    public Coup choisirCoup(Jeu jeu) {

        // Indice de la meilleure carte jouable trouvée
        // Initialisé à -1 pour indiquer qu'aucune carte jouable n'a encore été trouvée
        int indiceJouable = -1;

        // Valeur minimale trouvée parmi les cartes jouables
        int valeurMin = Integer.MAX_VALUE;

        // Parcours de toutes les cartes de la main du joueur courant
        for (int i = 0; i < jeu.getJoueurCourant().getMain().getNbCartes(); i++) {

            // Récupération de la carte à l'indice i
            Carte c = jeu.getJoueurCourant().getMain().voirCarte(i);

            // On vérifie si cette carte peut être posée sur l'expédition correspondante
            if (jeu.getJoueurCourant().getExpedition(c.getCouleur()).peutPoser(c)) {

                // Récupération de la valeur de la carte
                int valeur = c.getType().getValeur();

                // Si cette carte a une valeur plus petite que celles trouvées précédemment,
                // on la mémorise comme la meilleure carte à jouer
                if (valeur < valeurMin) {
                    valeurMin = valeur;
                    indiceJouable = i;
                }
            }
        }

        // Si au moins une carte jouable a été trouvée,
        // le bot choisit de jouer cette carte
        if (indiceJouable != -1) {
            return new Coup(indiceJouable, TypeCoup.JOUER);
        }

        // Sinon, aucune carte ne peut être jouée :
        // le bot défausse simplement la première carte de sa main
        return new Coup(0, TypeCoup.DEFAUSSER);
    }

    // Méthode appelée pour choisir d'où piocher
    // Le bot choisit toujours la pioche principale
    @Override
    public Couleur choisirPioche(Jeu jeu) {
        return null;
    }
}

