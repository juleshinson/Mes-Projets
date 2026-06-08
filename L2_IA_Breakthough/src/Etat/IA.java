package Etat;
import java.util.List;
import Evaluations.Evaluation;

public class IA {
    public static int eval0(EtatJeu e, Joueur j){
        int nbPionsJoueur = 0;  //pour compter le nombre de pions restant du joueur courant
        int nbPionsAdversaire = 0; //.... du joueur adverse

        for(int i = 0; i < 8; i++){
            for(int k = 0; k < e.getLargeur(); k++){
                Joueur contenu = e.getPlateau().getCase(i,k);
               if(contenu != null){ //on regarde chaque case et on incrémente si la case contient un pion
                   if(contenu.equals(j)){
                       nbPionsJoueur++; //du joueur
                   }
                   if(contenu.equals(j.autreJoueur())){
                       nbPionsAdversaire++; //ou de l'adversaire
                   }
               }
            }
        }
        return nbPionsJoueur - nbPionsAdversaire;
    }

    public static int evaluationMinimaxAvecEval0(EtatJeu e, Joueur machine, int profondeur){
        Gagnant gagnant = e.etatFinal();
        if(gagnant == Gagnant.BLANC) {
            if (machine == Joueur.joueurBlanc) {
                return 1000000;
            } else {
                return -1000000;
            }
        }
        if(gagnant == Gagnant.NOIR){
            if(machine == Joueur.joueurNoir){
                return 1000000;
            }else{
                return -1000000;
            }
        }
        if(profondeur == 0){
            return eval0(e, machine);
        }

        Joueur joueurCourant;
        if(e.getTourBlanc()){
            joueurCourant = Joueur.joueurBlanc;
        }else{
            joueurCourant = Joueur.joueurNoir;
        }

        List<EtatJeu> successeurs = e.successeurs(joueurCourant);
        if(successeurs.isEmpty()){
            return eval0(e, machine);
        }
        if(joueurCourant == machine){
            int meilleur = -1000000;
            for (EtatJeu suivant : successeurs) {
                int score = evaluationMinimaxAvecEval0(suivant, machine, profondeur - 1);
                if (score >= meilleur) {
                    meilleur = score;
                }
            }
            return meilleur;
        }else{
            int meilleur = 1000000;
            for (EtatJeu suivant : successeurs) {
                int score = evaluationMinimaxAvecEval0(suivant, machine, profondeur - 1);
                if (score < meilleur) {
                    meilleur = score;
                }
            }
            return meilleur;
        }
    }

    public static EtatJeu meilleurCoupMinimaxAvecEval0(EtatJeu e, Joueur machine, int profondeur) {
        Joueur joueurCourant;
        if (e.getTourBlanc()) {//On détermine le joueur qui doit jouer dans l'état courant
            joueurCourant = Joueur.joueurBlanc;
        } else {
            joueurCourant = Joueur.joueurNoir;
        }
        List<EtatJeu> listeSuccesseurs = e.successeurs(joueurCourant); //On calcule tous les successeurs possibles pour le joueur courant

        if (listeSuccesseurs.isEmpty()) { //Si aucun successeur n'existe, on renvoie simplement l'état courant
            return e;
        }

        EtatJeu meilleurEtat = listeSuccesseurs.getFirst(); //On prend le premier successeur comme meilleur état provisoire

        int meilleurScore = evaluationMinimaxAvecEval0(meilleurEtat, machine, profondeur - 1); //On calcule son score Minimax

        for (int i = 1; i < listeSuccesseurs.size(); i++){ //On parcourt ensuite tous les autres successeurs pour comparer leurs scores
            EtatJeu etatTeste = listeSuccesseurs.get(i); //On récupère le successeur courant
            int score = evaluationMinimaxAvecEval0(etatTeste, machine, profondeur - 1);//On calcule son score Minimax
            if (score > meilleurScore){//Si ce score est meilleur que le meilleur score actuel
                meilleurScore = score;
                meilleurEtat = etatTeste; //on met à jour le meilleur état et le meilleur score
            }
        }
        return meilleurEtat; //on renvoie l'état correspondant au meilleur coup trouvé
    }

    public static int evaluationMiniMaxAlphaBetaAvecEval(EtatJeu e, Joueur machine, int profondeur, Evaluation evaluation, int alpha, int beta) {
        //On vérifie si la partie est terminée
        Gagnant gagnant = e.etatFinal();

        if (gagnant == Gagnant.BLANC) { // Si Blanc gagne : si la machine est Blanc alors très bon score, sinon alors très mauvais score
            if (machine == Joueur.joueurBlanc) {
                return 1000000;
            } else {
                return -1000000;
            }
        }
        //pareil pour noir
        if (gagnant == Gagnant.NOIR) {
            if (machine == Joueur.joueurNoir) {
                return 1000000;
            } else {
                return -1000000;
            }
        }

        if (profondeur == 0) { //Si on a atteint la profondeur maximale, on utilise la fonction d'évaluation choisie
            return evaluation.eval(e, machine);
        }

        //On détermine quel joueur doit jouer
        Joueur joueurCourant;

        if (e.getTourBlanc()) {
            joueurCourant = Joueur.joueurBlanc;
        } else {
            joueurCourant = Joueur.joueurNoir;
        }

        //On récupère tous les successeurs possibles
        List<EtatJeu> successeurs = e.successeurs(joueurCourant);

        //Si aucun coup n'est possible, on évalue l'état
        if (successeurs.isEmpty()) {
            return evaluation.eval(e, machine);
        }

        //Cas 1 : c'est à la machine de jouer  (MAX)
        if (joueurCourant == machine) {

            for (EtatJeu suivant : successeurs) {
                //On appelle récursivement alpha-beta
                int score = evaluationMiniMaxAlphaBetaAvecEval(suivant, machine, profondeur - 1, evaluation, alpha, beta);
                if (score > alpha) {
                    alpha = score;
                }
                //condition d'élagage
                if (alpha >= beta) {
                    break;
                }
            }
            //Dans un nœud MAX, on renvoie alpha
            return alpha;
        }
        //Cas 2 : c'est l'adversaire (MIN)
        else {
            for (EtatJeu suivant : successeurs) {
                int score = evaluationMiniMaxAlphaBetaAvecEval(suivant, machine, profondeur - 1, evaluation, alpha, beta);
                //On met à jour beta si on trouve plus petit
                if (score < beta) {
                    beta = score;
                }
                //Coupure alpha-beta
                if (alpha >= beta) {
                    break;
                }
            }
            //Dans un noeud MIN, on renvoie beta
            return beta;
        }
    }

    public static EtatJeu meilleurCoupAlphaBetaAvecEval(EtatJeu e, Joueur machine, int profondeur, Evaluation evaluation) {

        Joueur joueurCourant = e.getTourBlanc() ? Joueur.joueurBlanc : Joueur.joueurNoir;
        List<EtatJeu> listeSuccesseurs = e.successeurs(joueurCourant);

        if (listeSuccesseurs.isEmpty()) {
            return e;
        }

        EtatJeu meilleurEtat = listeSuccesseurs.getFirst();
        int meilleurScore = Integer.MIN_VALUE;

        for (EtatJeu successeur : listeSuccesseurs) {
            // alpha = meilleurScore connu jusqu'ici, beta = +∞
            int score = evaluationMiniMaxAlphaBetaAvecEval(successeur, machine, profondeur - 1, evaluation, meilleurScore, 1000000);
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurEtat = successeur;
            }
        }
        return meilleurEtat;
    }
}
