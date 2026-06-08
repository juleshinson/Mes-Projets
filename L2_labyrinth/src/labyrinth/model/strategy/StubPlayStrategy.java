package labyrinth.model.strategy;

import labyrinth.model.Content;
import labyrinth.model.Direction;
import labyrinth.model.MazeMove;
import labyrinth.model.StartingPosition;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.collection.Position;

public class StubPlayStrategy implements PlayStrategy{
    @Override
    public Move getNextMove(GameDTO game) {
        Position actuelle = new Position(0,0) ;
        for(int i = 0 ; i < game.getMaze().getTiles().getWidth() ; i++){
            for(int j = 0 ; j < game.getMaze().getTiles().getHeight() ; j++) {
                Content c = game.getMaze().getTiles().get(i,j).getContent();
                if((c instanceof StartingPosition sp) && (sp.index() == game.getCurrentPlayerIndex())){
                    actuelle = new Position(i,j);
                }
            }
        }

        MazeMove mazeove =  new MazeMove(Direction.DOWN,0);
        return new Move(0,mazeove,actuelle);
    }
}
