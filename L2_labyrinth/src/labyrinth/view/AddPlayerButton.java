package labyrinth.view;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import labyrinth.viewmodel.PlayerViewModel;
import labyrinth.viewmodel.UserProfilesViewModel;

// pour optimiser l'affichage du bouton add
public class AddPlayerButton extends Button {
  private final UserProfilesViewModel userProfilesViewModel;
  private final PlayerViewModel playerViewModel;
  private final Menu addPlayerMenu = new Menu("Add player");
  private final MenuItem invitedPlayerMenuItem = new MenuItem("Invited");
  private final SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

  public AddPlayerButton(
      PlayerViewModel playerViewModel, UserProfilesViewModel userProfilesViewModel) {
    super("+");
    this.userProfilesViewModel = userProfilesViewModel;
    this.playerViewModel = playerViewModel;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem addBotMenuItem = new MenuItem("Add bot");
    addBotMenuItem.setOnAction(e -> playerViewModel.addBot());
    contextMenu.getItems().addAll(addPlayerMenu, addBotMenuItem);
    setOnAction(e -> contextMenu.show(this, Side.BOTTOM, 0, 0));

    invitedPlayerMenuItem.setOnAction(e -> playerViewModel.addPlayer());

    populateAddPlayerMenu();
    InvalidationListener updateListener = obs -> populateAddPlayerMenu();
    userProfilesViewModel.getUserProfiles().addListener(updateListener);
    userProfilesViewModel.getSelectedUserProfiles().addListener(updateListener);
  }

  private void populateAddPlayerMenu() {
    addPlayerMenu.getItems().setAll(invitedPlayerMenuItem, separatorMenuItem);
    List<MenuItem> menuItems =
        userProfilesViewModel.getUserProfiles().stream()
            .map(
                p -> {
                  MenuItem menuItem = new MenuItem(p.name());
                  menuItem.setOnAction(e -> playerViewModel.addPlayer(p));
                  menuItem.setDisable(userProfilesViewModel.getSelectedUserProfiles().contains(p));
                  return menuItem;
                })
            .toList();
    addPlayerMenu.getItems().addAll(menuItems);
  }
}
