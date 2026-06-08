package labyrinth.viewmodel;

import javafx.collections.ObservableList;
import labyrinth.service.persistence.UserProfile;

public interface UserProfilesViewModel {
  // liste de tous les joueurs définis dans la BD
  ObservableList<UserProfile> getUserProfiles();

  // liste des joueurs sélectionnés dans le jeu actuel, ils ne doivent pas être retirés de la BD
  ObservableList<UserProfile> getSelectedUserProfiles();

  void createUserProfile(String userName);

  void renameUserProfile(UserProfile userProfile, String userName);

  void deleteUserProfile(UserProfile userProfile);
}
