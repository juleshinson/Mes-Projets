package modele.strategie;

import modele.Coup;
import modele.Jeu;
import modele.TypeCoup;
import modele.carte.Couleur;

// La classe BotDefausse représente une stratégie très simple.
// Ce bot ne réfléchit pas : il défausse toujours la première carte de sa main et pioche toujours dans la pioche principale.
public class BotDefausse implements Strategie {

    // Méthode appelée pour choisir le coup à jouer
    // Ici, le bot défausse systématiquement la carte d'indice 0
    @Override
    public Coup choisirCoup(Jeu jeu) {
        return new Coup(0, TypeCoup.DEFAUSSER);
    }

    // Méthode appelée pour choisir d'où piocher
    // Le bot choisit toujours la pioche principale
    @Override
    public Couleur choisirPioche(Jeu jeu) {
        return null;
    }
}

