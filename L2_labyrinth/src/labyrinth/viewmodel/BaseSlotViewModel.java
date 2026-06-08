package labyrinth.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;

public class BaseSlotViewModel implements  SlotViewModel{

        private final MazeMove mazeMove ;
        private final BaseMazeViewModel mazeViewModel;
        private final BooleanProperty magnetizeProperty = new SimpleBooleanProperty(false);
        private final BooleanProperty forbidden = new SimpleBooleanProperty(false);
        private final BooleanProperty activeProperty = new SimpleBooleanProperty(false);

        public BaseSlotViewModel(MazeMove mazeMove,BaseMazeViewModel mazeViewModel){
            this.mazeMove = mazeMove;
            this.mazeViewModel = mazeViewModel ;

            forbidden.bind(Bindings.createBooleanBinding(() -> mazeViewModel.forbiddenMoveProperty().getValue().equals(this.mazeMove),
                    mazeViewModel.forbiddenMoveProperty())
            );
        }

        public MazeMove getMazeMove() {
            return mazeMove;
        }


        public BaseTileViewModel getExtraTileViewModel() {
            return new BaseTileViewModel();
        }


        public ObservableValue<Boolean> magnetizedProperty() {
            return this.magnetizeProperty;
        }


        public void magnetize() {
            if(!this.isForbidden() && mazeViewModel.getState()== GameState.MAZE_MOVE) {
                magnetizeProperty.setValue(true);
            }
        }

        public ObservableValue<Boolean> activeProperty(){
            return activeProperty ;
        }

        public boolean isActive(){
            return activeProperty.getValue();
        }

        public void setActive(boolean value){
            activeProperty.setValue(value);
        }

        public void demagnetize() {
            magnetizeProperty.setValue(false);
        }


        public boolean isMagnetized() {
            return magnetizeProperty.getValue();
        }


        public ObservableValue<Boolean> forbiddenProperty() {
            return forbidden;
        }


        public void moveMaze() {
            mazeViewModel.moveMaze(this.mazeMove);
        }


        public boolean isForbidden() {
            return forbidden.get();
        }



}


