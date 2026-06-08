package labyrinth.model;


import labyrinth.model.strategy.Move;
import labyrinth.model.strategy.PlayStrategy;
import labyrinth.model.strategy.StubPlayStrategy;
import labyrinth.model.strategy.simulation.DefaultPlayStrategy;
import labyrinth.service.serialization.GameDTO;
import labyrinth.service.serialization.PlayerDTO;
import labyrinth.util.MazeBuilder;
import labyrinth.util.collection.Grid;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Game {
    public static final PlayStrategy STRATEGY = new DefaultPlayStrategy();
    private Maze maze;
    private Slot[] slots ;
    private SimpleObservableValue<GameState> state ;
    private SimpleObservableValue<Player> currentPlayer ; // joueur courant
    // identifiant unique de la partie : généré à la creation
    private UUID uuid;

    public Game() {
        this.slots = new Slot[4];
        this.state = new SimpleObservableValue<>(GameState.SETUP);
        this.currentPlayer = new SimpleObservableValue<>(null);
        this.uuid = UUID.randomUUID();
        reset();
    }

    //pour permettre la modification du nombre de joueurs par exemple en CLI
    public Game(int nbJoueurs){
        this.slots = new Slot[nbJoueurs];
        this.state = new SimpleObservableValue<>(GameState.SETUP);
        this.currentPlayer = new SimpleObservableValue<>(null);
        this.uuid = UUID.randomUUID();
        reset();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Slot[] getSlots() {
        return slots;
    }

    public Maze getMaze() {
        return maze;
    }

    public Tile getTile(Player player) {
        return maze.getTile(player);
    }

    public Slot getSlot(int index){
        if (index < 0 || index >= slots.length) return null;
        return slots[index];
    }

    public ObservableValue<GameState> stateProperty() {
        return state.asReadOnly();
    }

    public GameState getState(){
        return state.getValue() ;
    }

    public ObservableValue<Player> currentPlayerProperty() {
        return currentPlayer.asReadOnly();
    }

    public Player getCurrentPlayer(){
        return currentPlayer.getValue() ;
    }

    public void reset(){
        this.maze = null ; //je détruit le labyrinthe
        this.currentPlayer.setValue(null);
        state.setValue(GameState.SETUP); ;
        for(int i = 0 ; i< slots.length ; i++){
            if (this.slots[i] == null) {
                // premier appel depuis le constructeur : les slots n'existent pas encore
                this.slots[i] = new Slot(this, i);
            } else if(this.slots[i].getPlayer() != null){
                // réinitialisation en cours de partie : on vide juste le deck sans recréer le slot
                this.slots[i].getPlayer().getDeck().clear();
            }
        }
    }

    // Réinitialisation complète : remet en SETUP ET ferme tous les slots
    public void hardReset() {
        // on passe d'abord en SETUP pour que close() soit autorisé
        state.setValue(GameState.SETUP);
        this.maze = null;
        this.currentPlayer.setValue(null);
        for(int i = 0; i < slots.length; i++){
            if(slots[i] == null){
                slots[i] = new Slot(this, i);
            } else if(slots[i].getPlayer() != null){
                slots[i].close();
            }
        }
    }

    public void init(){
        if(state.getValue() != GameState.SETUP){
            throw new IllegalStateException() ;
        }else {
            this.maze = MazeBuilder.buildStandardMaze();
            // 24 trésors au total (IDs 0 à 23), distribués équitablement entre les joueurs
            List<Integer> tresors = new ArrayList<>();
            for(int i = 0 ; i < 24 ; i++){
                tresors.add(i);
            }
            Collections.shuffle(tresors);

            // compter les slots actifs pour la distribution des trésors et le choix du premier joueur
            int nbActifs = 0;
            for(int i = 0; i < slots.length; i++){
                if(slots[i].getPlayer() != null) nbActifs++;
            }
            if(nbActifs == 0) throw new IllegalStateException("Aucun joueur pour démarrer la partie");

            for (int i = 0; i < slots.length; i++) {
                if (this.slots[i].getPlayer() == null) {
                    continue;  // slot vide, on le saute
                }
                getMaze().getStarting(slots[i].getPlayer()).addPlayer(slots[i].getPlayer());
                List<Integer> tresorsplayer = new ArrayList<>() ;
                for(int j = 0 ; j < 24 / nbActifs ; j++){  // 24 / nb de joueurs réels
                    tresorsplayer.add(tresors.remove(0));
                }
                this.slots[i].getPlayer().getDeck().init(tresorsplayer) ;
            }

            int[] compteurs = new int[slots.length];
            for (Grid.Entry<Tile> entry : maze.getTiles()) {
                for (Player p : entry.value().getPlayers()) {
                    int id = p.getIndex();
                    if (id < 0 || id >= slots.length) {
                        throw new IllegalArgumentException("Joueur avec un id invalide");
                    }
                    compteurs[id]++;
                }
            }
            for (int i = 0; i < slots.length; i++) {
                if (slots[i].getPlayer() == null) continue;  // slot vide donc pas de vérification
                if (compteurs[i] == 0) {
                    throw new IllegalStateException("Le joueur " + i + " n'a pas de position de départ");
                }
                if (compteurs[i] > 1) {
                    throw new IllegalStateException("Le joueur " + i + " apparaît sur plusieurs tuiles");
                }
            }

            // choisir aléatoirement parmi les joueurs actifs
            List<Integer> actifs = new ArrayList<>();
            for(int i = 0; i < slots.length; i++){
                if(slots[i].getPlayer() != null) actifs.add(i);
            }
            int index = actifs.get((int)(actifs.size() * Math.random()));
            currentPlayer.setValue(slots[index].getPlayer());
            state.setValue(GameState.MAZE_MOVE);
        }
    }

    public void rotateExtraTile(int nbTimes){
        if(state.getValue() != GameState.MAZE_MOVE) {
            throw new IllegalStateException();
        }else {
            maze.getExtraTile().rotate(nbTimes);
        }
    }

    public void moveMaze(MazeMove move){
        if(state.getValue() != GameState.MAZE_MOVE){
            throw new IllegalStateException();
        }else{
            maze.move(move) ;
            state.setValue(GameState.PLAYER_MOVE);
        }
    }

    public void movePlayer(Tile tile){
        if(state.getValue() != GameState.PLAYER_MOVE){
            throw new IllegalStateException();
        }if(tile == null){
            throw new IllegalArgumentException();
        }else{
            maze.getTile(currentPlayer.getValue()).removePlayer(currentPlayer.getValue());
            tile.addPlayer(currentPlayer.getValue());

            if (!currentPlayer.getValue().getDeck().getDrawPile().isEmpty()
                    && tile.getContent() instanceof Treasure t
                    && t.id() == currentPlayer.getValue().getDeck().getDrawPile().getFirst()) {
                //il a trouvé son trésor
                currentPlayer.getValue().getDeck().discard();
                tile.setContent(new None());  // efface le trésor de la tuile
                List<Integer> trouves = currentPlayer.getValue().getDeck().getDiscardPile();
                StringBuilder sb = new StringBuilder();
                sb.append("Joueur ").append(currentPlayer.getValue().getIndex() + 1)
                  .append(" — trésors trouvés (").append(trouves.size()).append(") : ");
                for (int id : trouves) sb.append((char)('A' + id)).append(" ");
                System.out.println(sb);
            }
            if(currentPlayer.getValue().hasWon()){
                state.setValue(GameState.GAME_OVER);
            }else{
                state.setValue(GameState.TURN_END);
            }
        }
    }

    public void startTurn(){
        if(getCurrentPlayer() != null && getCurrentPlayer().getIsBot()){
            GameDTO snapshot = new GameDTO(this);
            new Thread(() -> {
                Move coup = null;
                try {
                    coup = STRATEGY.getNextMove(snapshot);
                } catch (Exception e) {
                    System.out.println("Bot stratégie erreur : " + e.getMessage());
                }
                if(coup == null) return;
                int nbRotations = coup.nbRotations();
                MazeMove mazeMove = coup.mazeMove();
                int x = coup.destination().x();
                int y = coup.destination().y();
                javafx.application.Platform.runLater(() -> {
                    try {
                        rotateExtraTile(nbRotations);
                        moveMaze(mazeMove);
                        movePlayer(maze.getTiles().get(x, y));
                    } catch (Exception e) {
                        System.out.println("Bot coup erreur : " + e.getMessage());
                    }
                });
            }).start();
        }
    }
    public void endTurn(){
        if(state.getValue() != GameState.TURN_END) {
            throw new IllegalStateException();
        }else{
            if(getCurrentPlayer() == null) return;
            int nextIndex = (getCurrentPlayer().getIndex() + 1) % slots.length;
            int attempts = 0;
            while(slots[nextIndex].getPlayer() == null && attempts < slots.length) {
                nextIndex = (nextIndex + 1) % slots.length;
                attempts++;
            }
            currentPlayer.setValue(slots[nextIndex].getPlayer());
            state.setValue(GameState.MAZE_MOVE) ;
        }
        startTurn();
    }

    public void updateFrom(GameDTO gameDTO){
        // on repasse en setup pour pouvoir modifier les slots
        state.setValue(GameState.SETUP);
        for(int i = 0; i < slots.length; i++){
            if(slots[i].getPlayer() != null){
                slots[i].close();
            }
        }
        for(int i = 0 ; i < slots.length ; i++) {
            PlayerDTO playerDTO = gameDTO.getPlayers().get(i);
            if (playerDTO != null) {
                slots[i].open();
            } else {
                slots[i].close();
            }
        }
        // on reconstruit la liste des joueurs pour passer à Maze.from
        List<Player> joueurs = new ArrayList<>();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].getPlayer() != null) {
                joueurs.add(slots[i].getPlayer());
            }
        }

        // maintenant on recrée le labyrinthe depuis le DTO
        this.maze = Maze.from(gameDTO.getMaze(), joueurs);

        // on remet l'état et le joueur courant
        state.setValue(gameDTO.getState());

        // on retrouve le joueur courant par son index
        int indexCourant = gameDTO.getCurrentPlayerIndex();
        for (Player p : joueurs) {
            if (p.getIndex() == indexCourant) {
                currentPlayer.setValue(p);
                break;
            }
        }
    }
}
