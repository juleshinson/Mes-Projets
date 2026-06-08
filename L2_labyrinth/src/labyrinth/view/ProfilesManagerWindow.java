package labyrinth.view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import labyrinth.service.persistence.UserProfile;
import labyrinth.viewmodel.UserProfilesViewModel;

public class ProfilesManagerWindow extends Stage {
  private final UserProfilesViewModel userProfilesViewModel;

  public ProfilesManagerWindow(UserProfilesViewModel userProfilesViewModel) {
    this.userProfilesViewModel = userProfilesViewModel;
    setTitle("Profil Manager");

    ListView<UserProfile> listView = new ListView<>();
    listView.setSelectionModel(null);
    listView.setFocusTraversable(false);
    listView.setCellFactory(l -> new Cell());
    listView.setItems(userProfilesViewModel.getUserProfiles());
    listView.setPrefSize(300, 400);

    Button addButton = new Button("New");
    addButton.setOnAction(e -> userProfilesViewModel.createUserProfile("New profile"));

    VBox root = new VBox(listView, addButton);
    root.setPadding(new Insets(5));
    root.setSpacing(5);
    Scene scene = new Scene(root);
    setScene(scene);
    sizeToScene();
  }

  private class Cell extends ListCell<UserProfile> {
    private final Button menuButton = new Button("⚙");
    private final MenuItem deleteMenuItem = new MenuItem("Delete");
    private final Label nameLabel = new Label();

    private final TextField newNameTextField = new TextField();
    private final Button validationButton = new Button("Ok");
    private final HBox modificationContainer = new HBox(newNameTextField, validationButton);

    private final HBox graphic = new HBox(menuButton, nameLabel, modificationContainer);

    public Cell() {

      setText(null);
      userProfilesViewModel.getSelectedUserProfiles().addListener((Observable obs) -> update());

      ContextMenu menu = new ContextMenu();

      MenuItem renameMenuItem = new MenuItem("Rename");
      renameMenuItem.setOnAction(
          e -> {
            newNameTextField.setText(getItem().name());
            newNameTextField.selectAll();
            modificationContainer.setVisible(true);
            newNameTextField.requestFocus();
          });

      deleteMenuItem.setOnAction(e -> userProfilesViewModel.deleteUserProfile(this.getItem()));

      menu.getItems().addAll(renameMenuItem, deleteMenuItem);

      menuButton.setPadding(new Insets(0));
      menuButton.setPrefSize(20, 20);
      menuButton.setOnMouseClicked(
          e -> {
            if (menu.isShowing()) {
              menu.hide();
            } else {
              menu.show(menuButton, Side.BOTTOM, 0, 0);
            }
          });

      nameLabel.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(nameLabel, Priority.ALWAYS);

      modificationContainer.setVisible(false);
      newNameTextField.setPrefWidth(150);
      InvalidationListener listener =
          obs ->
              modificationContainer.setVisible(
                  newNameTextField.isFocused() || validationButton.isFocused());
      newNameTextField.focusedProperty().addListener(listener);
      validationButton.focusedProperty().addListener(listener);

      validationButton.setOnAction(
          e -> {
            userProfilesViewModel.renameUserProfile(this.getItem(), newNameTextField.getText());
            modificationContainer.setVisible(false);
          });

      graphic.setAlignment(Pos.CENTER_LEFT);
      graphic.setSpacing(5);
      graphic.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateItem(UserProfile item, boolean isEmpty) {
      super.updateItem(item, isEmpty);
      if (isEmpty || item == null) {
        setGraphic(null);
      } else {
        nameLabel.setText(item.name());
        setGraphic(graphic);
        update();
      }
    }

    private void update() {
      deleteMenuItem.setDisable(
          userProfilesViewModel.getSelectedUserProfiles().contains(getItem()));
    }
  }
}
