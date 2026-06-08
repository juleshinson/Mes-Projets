package labyrinth.viewmodel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import labyrinth.model.Game;
import labyrinth.service.Services;
import labyrinth.service.persistence.UserProfile;

import java.util.List;

public class BaseUserProfilesViewModel implements UserProfilesViewModel {

    private final Services services ;
    private final Game game ;
    private final ObservableList<UserProfile> userProfiles = FXCollections.observableArrayList() ;
    private final ObservableList<UserProfile> selectedUserProfiles = FXCollections.observableArrayList() ;

    public BaseUserProfilesViewModel(Game game, Services services) {
        this.game = game ;
        this.services = services ;
        rafraichirListe() ;

        // on s'abonne aux modifs de profils pour mettre à jour la liste automatiquement
        if (services.getPersistenceService() != null) {
            services.getPersistenceService().ajouterProfileListener(uuid -> {
                Platform.runLater(this::rafraichirListe) ;
            }) ;
        }
    }

    private void rafraichirListe() {
        if (services.getPersistenceService() == null) {
            return;
        }
        List<UserProfile> profils = services.getPersistenceService().getUserProfiles() ;
        userProfiles.setAll(profils) ;
    }

    @Override
    public ObservableList<UserProfile> getUserProfiles() {
        return userProfiles ;
    }

    @Override
    public ObservableList<UserProfile> getSelectedUserProfiles() {
        return selectedUserProfiles ;
    }

    @Override
    public void createUserProfile(String userName) {
        if (services.getPersistenceService() == null) {
            return;
        }
        services.getPersistenceService().createUserProfile(userName) ;
    }

    @Override
    public void renameUserProfile(UserProfile userProfile, String userName) {
        if (services.getPersistenceService() == null) {
            return;
        }
        services.getPersistenceService().setUserName(userProfile.uuid(), userName) ;
    }

    @Override
    public void deleteUserProfile(UserProfile userProfile) {
        if (services.getPersistenceService() == null) {
            return;
        }
        // on vérifie quand meme qu'il n'est pas dans la partie en cours
        if (selectedUserProfiles.contains(userProfile)) {
            System.out.println("impossible de supprimer un profil en cours de partie") ;
            return ;
        }
        services.getPersistenceService().deleteUserProfile(userProfile.uuid()) ;
    }
}