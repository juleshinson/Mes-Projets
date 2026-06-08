package labyrinth.view;


import javafx.scene.layout.VBox;
import labyrinth.viewmodel.PlayerViewModel;
import labyrinth.viewmodel.UserProfilesViewModel;

import java.util.List;

public class PlayersView extends VBox {

    public PlayersView(List<PlayerViewModel> players, UserProfilesViewModel userProfilesViewModel){
        //on ajoute chaque joueur au VBOX
        for(PlayerViewModel player: players){
            this.getChildren().add(new PlayerView(player, userProfilesViewModel));
        }
    }
}
