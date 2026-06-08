package labyrinth.model.strategy.simulation;

import labyrinth.model.MazeMove;
import labyrinth.model.strategy.Move;
import labyrinth.model.strategy.PlayStrategy;
import labyrinth.model.strategy.ScoredMove;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Graph;
import labyrinth.util.graph.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultPlayStrategy implements PlayStrategy {
    private static final int NB_RANDOM_MOVES = 12;
    private final int N = 10;        // nombre de simulations Monte‑Carlo
    private final double ALPHA = 0.5; // poids de l’ajustement


    private GameSimulation game;
    private PlayerSimulation initialPlayer;

    @Override
    public Move getNextMove(GameDTO snapshot) {
        game = new GameSimulation(snapshot);
        initialPlayer = game.getCurrentPlayer();
        return computeNextMove();
    }

    private int getScore() {
        if (game.hasWon(initialPlayer)) {
            return 10000;
        }

        for (PlayerSimulation playerSimulation : game.getPlayers()) {
            if (playerSimulation != null && playerSimulation != initialPlayer && game.hasWon(playerSimulation)) {
                return -10000;
            }
        }

        int n = initialPlayer.getDiscardPile().size();

        // distance BFS-aware : si la cible est inaccessible dans ce labyrinthe, on pénalise
        // plutôt que d'utiliser la distance de Manhattan (qui peut être petite même si le chemin
        // réel est infini — ex. coin isolé avec trésor dans la même rangée fixe)
        int nextTreasure = initialPlayer.getNextTreasure();
        TileSimulation targetTile = nextTreasure == -1
                ? game.getMaze().getStartingTile(initialPlayer)
                : game.getMaze().getTreasureTile(nextTreasure);
        TileSimulation playerTile = game.getMaze().getPlayerTile(initialPlayer);
        Graph<TileSimulation> graph = MazeSimulationAdapter.getGraph(game.getMaze());
        Paths<TileSimulation> reachability = new Paths<>(graph, playerTile);

        // compter les cases accessibles depuis le joueur (bonus d'exploration)
        int reachableCount = 0;
        int w = game.getMaze().getTiles().getWidth();
        int h = game.getMaze().getTiles().getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (reachability.isAccessible(game.getMaze().getTiles().get(i, j))) reachableCount++;
            }
        }

        int d;
        if (targetTile == null || targetTile.getPosition() == null) {
            d = 50; // cible hors grille (tuile extra) — forte pénalité (était 15 : un chemin accessible lointain scorait moins bien que l'inaccessible)
        } else {
            // distance BFS réelle (était Manhattan : d=2 Manhattan peut masquer un chemin réel de 20 pas à cause des murs)
            // pénalité 50 pour l'inaccessible garantit qu'on préfère toujours un accessible même très lointain
            d = reachability.isAccessible(targetTile)
                    ? reachability.getPath(targetTile).size() - 1
                    : 50;
        }

        // + reachableCount/5 : léger bonus pour les coups qui ouvrent plus de cases
        return 200 * n - d + reachableCount / 5;
    }

    private Move computeNextMove() {

        if (game.hasWon(initialPlayer)) return null;

        List<ScoredMove> moves = computeMoves();

        int k = 5 ;
        moves.sort((a,b)-> Integer.compare(b.score(), a.score()));
        List<ScoredMove> candidates = moves.subList(0,Math.min(k,moves.size()));

        ScoredMove best = null ;
        int bestScore = Integer.MIN_VALUE ;

        for(ScoredMove scoredMove : candidates){
            ScoredMove adjusted = adjust(scoredMove,5);

            if(adjusted.score() > bestScore){
                bestScore = adjusted.score() ;
                best = adjusted ;
            }
        }

        // best peut être null si adjust() a lancé une exception pour tous les candidats
        if (best == null) best = candidates.isEmpty() ? moves.getFirst() : candidates.getFirst();
        return best.move();
    }



    private int evaluateState() {
       if (game.hasWon(initialPlayer)) {
           return 1_000_000;
       }

       // Plus la distance est petite, mieux c’est
        int dist = game.getDistanceToTarget(initialPlayer);
        return -dist ;
    }
    private List<ScoredMove> computeMoves() {

        List<ScoredMove> results = new ArrayList<>();

        //  Explorer tous les MazeMove possibles
        for (MazeMove mazeMove : game.getValidMazeMoves()) {

            //  Essayer les 4 rotations possibles
            for (int rot = 0; rot < 4; rot++) {

                // --- APPLY MAZE MODIFICATION ---
                game.modifyMaze(rot, mazeMove);

                //  Calculer les cases atteignables
                Paths<TileSimulation> paths = game.computePaths();

                for (int i = 0; i < game.getMaze().getTiles().getWidth(); i++) {
                    for (int j = 0; j < game.getMaze().getTiles().getHeight(); j++) {

                        TileSimulation dest = game.getMaze().getTiles().get(i, j);

                        if (!paths.isAccessible(dest)) continue;


                        game.movePlayer(dest);

                        //  Calculer le score
                        int score = getScore();

                        //  Rollback du déplacement
                        game.undoLastPlayerMove();

                        //  Stocker le coup
                        Move move = new Move(rot, mazeMove, dest.getPosition());
                        results.add(new ScoredMove(move, score));
                    }
                }

                game.undoLastMazeModification();
            }
        }

        return results;
    }

    private Move getBestRandomMove(int nb) {

        ScoredMove best = null;

        for (int i = 0; i < nb; i++) {

            ScoredMove sm = getRandomMove(); // un coup aléatoire + score
            if (best == null || sm.score() > best.score()) {
                best = sm;
            }
        }

        return best.move();
    }
    private ScoredMove getRandomMove() {

        Random rng = new Random();

        //  MazeMove aléatoire
        List<MazeMove> mazeMoves = game.getValidMazeMoves();
        MazeMove mazeMove = mazeMoves.get(rng.nextInt(mazeMoves.size()));

        //  Rotation aléatoire
        int rot = rng.nextInt(4);


        game.modifyMaze(rot, mazeMove);

        // 3) Cases atteignables
        Paths<TileSimulation> paths = game.computePaths();
        List<TileSimulation> reachable = new ArrayList<>();

        for (int i = 0; i < game.getMaze().getTiles().getWidth(); i++) {
            for (int j = 0; j < game.getMaze().getTiles().getHeight(); j++) {
                TileSimulation t = game.getMaze().getTiles().get(i, j);
                if (paths.isAccessible(t)) reachable.add(t);
            }
        }

        //  Choisir la destination la plus proche de l’objectif
        TileSimulation bestDest = null;
        int bestDist = Integer.MAX_VALUE;

        for (TileSimulation t : reachable) {
            PlayerSimulation current = game.getCurrentPlayer();
            int d = game.getDistanceToTarget(t.getPosition(), current);

            if (d < bestDist) {
                bestDist = d;
                bestDest = t;
            }
        }


        if (bestDest == null) {
            bestDest = reachable.get(rng.nextInt(reachable.size()));
        }

        Position destPosition = bestDest.getPosition();


        game.movePlayer(bestDest);


        int score = getScore();


        game.undoLastPlayerMove();
        game.undoLastMazeModification();

        Move move = new Move(rot, mazeMove, destPosition);
        return new ScoredMove(move, score);
    }


    private int simulate(int maxDepth) {


        int movesPlayed = 0;

        // On simule jusqu'à maxDepth coups
        for (int depth = 0; depth < maxDepth; depth++) {

            // Si la partie est finie → on arrête
            if (game.isGameOver()) {
                break;
            }

            // Coup aléatoire intelligent
            Move randomMove = getBestRandomMove(NB_RANDOM_MOVES);

            // Appliquer le coup
            game.apply(randomMove);
            movesPlayed++;
        }

        // Score final après la simulation
        int finalScore = getScore();

        // Rollback complet
        for (int i = 0; i < movesPlayed; i++) {
            game.undoLastMove();
        }

        return finalScore;
    }

    private ScoredMove adjust(ScoredMove scoredMove, int maxDepth) {

        Move move = scoredMove.move();


        game.apply(move);

        double sum = 0;


        for (int i = 0; i < N; i++) {
            sum += simulate(maxDepth);
        }


        game.undoLastMove();

        // --- score ajusté ---
        double adjustedScore = scoredMove.score() + ALPHA * (sum / N);

        return new ScoredMove(move, (int) adjustedScore);
    }

}