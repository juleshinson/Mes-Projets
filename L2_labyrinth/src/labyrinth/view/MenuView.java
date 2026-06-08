package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import labyrinth.viewmodel.GameViewModel;


public class MenuView extends BorderPane {

    public MenuView(GameViewModel gameViewModel){
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        MenuItem saveGame = new MenuItem("Save Game");
        MenuItem loadGame = new MenuItem("Load Game");
        MenuItem exit = new MenuItem("Exit");

        fileMenu.getItems().addAll(newGame, saveGame, loadGame, exit);
        menuBar.getMenus().add(fileMenu);

        Button manageProfilesBtn = new Button("Gérer les profils");
        manageProfilesBtn.setOnAction(e -> gameViewModel.getMenuViewModel().manageProfiles());

        HBox topBar = new HBox(menuBar, manageProfilesBtn);
        topBar.setSpacing(8);
        topBar.setStyle("-fx-alignment: center-left; -fx-padding: 2 4;");

        newGame.setOnAction(even -> gameViewModel.getMenuViewModel().newGame());
        saveGame.setOnAction(even -> gameViewModel.getMenuViewModel().saveGame());
        loadGame.setOnAction(even -> gameViewModel.getMenuViewModel().loadGame());
        exit.setOnAction(event -> gameViewModel.getMenuViewModel().exit());

        this.setTop(topBar);
    }


}
