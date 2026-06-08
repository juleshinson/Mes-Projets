package labyrinth.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import labyrinth.util.collection.Grid;
import labyrinth.viewmodel.*;
import java.util.ArrayList;
import java.util.List;



public class BoardView extends StackPane {



    public BoardView(MazeViewModel mazeViewModel) {

        //initialisation du grid
        GridPane gridPane = new GridPane();

        //ajout du gridPane
        this.getChildren().add(gridPane);


        Grid<CellViewModel> grid = mazeViewModel.getCellViewModels();
        for (int i = 0; i < grid.getWidth() + 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPrefWidth(50);
            cc.setMinWidth(50);
            cc.setMaxWidth(50);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int j = 0; j < grid.getHeight() + 2; j++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(50);
            rc.setMinHeight(50);
            rc.setMaxHeight(50);
            gridPane.getRowConstraints().add(rc);
        }


        for (int i = 0; i < grid.getWidth()  ; i++) {
            for (int j = 0; j < grid.getHeight() ; j++) {
                CellViewModel cellVM = grid.get(i, j);
                CellView cellView = new CellView(cellVM);
                gridPane.add(cellView,i+1,j+1);
                }
        }


        int width = grid.getWidth();
        int height = grid.getHeight();
        List<SlotViewModel> slots = new ArrayList<>(mazeViewModel.getSlotViewModels().values());


        //Création, initialisation et ajout des slotView au gridPane
        // TOP
        for (int j = 0; j < width; j++) {

            SlotView slot = new SlotView(slots.get(j), mazeViewModel.getExtraTileViewModel());
            if(j%2!=0) {  //on prend les impairs
                gridPane.add(slot, j + 1, 0);
            }
        }

        // BOTTOM
        for (int j = 0; j < width; j++) {

            SlotView slot = new SlotView(slots.get(width + j), mazeViewModel.getExtraTileViewModel());
            if(j%2!=0) {
                gridPane.add(slot, j + 1, height + 1);
            }
        }

        // LEFT
        for (int k = 0; k < height; k++) {
            SlotView slot = new SlotView(slots.get(2*width+k), mazeViewModel.getExtraTileViewModel());
            if(k%2 !=0) {
                gridPane.add(slot, 0, k + 1);
            }
        }


        // RIGHT
        for (int k = 0; k < height; k++) {
            SlotView slot = new SlotView(slots.get(3*width+k), mazeViewModel.getExtraTileViewModel());
            if(k%2 != 0) {
                gridPane.add(slot, width + 1, k + 1);
            }
        }

    }


    }




