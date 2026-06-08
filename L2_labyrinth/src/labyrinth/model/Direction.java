package labyrinth.model;

public enum Direction {
 //   UP,RIGHT,DOWN, LEFT ;
 UP(0), RIGHT(90), DOWN(180), LEFT(270);
    private int angle;

    private Direction(int angle) {
        this.angle = angle;
    }
    public Direction getOpposite(){
        Direction res = null;
        switch (this) {
            case UP :
                res = DOWN ;
                break ;
            case DOWN :
                res = UP ;
                break ;
            case RIGHT:
                res = LEFT ;
                break ;
            case LEFT :
                res = RIGHT ;
        }
        return res ;
    }

    public int getAngle () {
        return angle;
    }

    public Direction getRotated(int nbTimes){
        Direction[] directions = Direction.values() ; // on recupere le tableau des enums
        int n = ((this.ordinal() + nbTimes) % directions.length + directions.length) % directions.length ;
        return directions[n] ; //on renvoie l'enum correspndant
    }

    public Direction getRotated(Direction direction){
        return getRotated(direction.ordinal()); //on delègue a la fonction précédente on lui envoyant l'indice de la direction voulue
    }
}

