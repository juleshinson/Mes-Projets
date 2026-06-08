package labyrinth.model;

public record MazeMove(Direction direction, int index) {

    public MazeMove getOpposite(){
        return switch (direction) {
            case UP -> new MazeMove(Direction.DOWN, index);
            case DOWN -> new MazeMove(Direction.UP, index);
            case LEFT -> new MazeMove(Direction.RIGHT, index);
            case RIGHT -> new MazeMove(Direction.LEFT, index);
        };
    }
}
