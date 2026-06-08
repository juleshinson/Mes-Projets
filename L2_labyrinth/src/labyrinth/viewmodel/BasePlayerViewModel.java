package labyrinth.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Slot;
import labyrinth.service.Services;
import labyrinth.service.persistence.UserProfile;

import java.util.UUID;

public class BasePlayerViewModel implements PlayerViewModel {
    private final int index ;
    private final StringProperty nameProperty = new SimpleStringProperty("Indisponible");
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty lockedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isBotProperty = new SimpleBooleanProperty(false);
    private final Services services;
    private Slot slot ;
    public BasePlayerViewModel(int index){
        this(index, null,null);

    }

    public BasePlayerViewModel(int index, Services services,Slot slot){
        this.index = index ;
        this.services = services ;
        this.slot = slot ;
    }

    public int getIndex() {
        return index;
    }

    public String getNameProperty(){
        return nameProperty.getValue();
    }


    public ObservableValue<Boolean> activeProperty() {
        return activeProperty;
    }


    public boolean isActive() {
        return activeProperty.getValue();
    }

    public ObservableValue<String> nameProperty() {
        return nameProperty;
    }


    public String getName() {
        return nameProperty.getValue();
    }


    public ObservableValue<Boolean> lockedProperty() {
        return lockedProperty;
    }


    public boolean isLocked() {
        return lockedProperty.getValue();
    }


    public void addPlayer() {
        if(!isLocked()){
            slot.setupIfNeeded();  // remet en SETUP si une partie était chargée depuis la BDD
            activeProperty.setValue(true);
            nameProperty.setValue("Joueur "+ (index+1));
            slot.open();
        }
    }

    public void setLockedProperty(Boolean locked){
        lockedProperty.setValue(locked);
    }

    public void addBot() {
        if(!lockedProperty.getValue()){
            slot.setupIfNeeded();
            activeProperty.setValue(true);
            nameProperty.setValue("Bot "+ (index+1));
            isBotProperty.setValue(true);
            slot.open(true);
        }
    }

    public void addPlayer(UUID profileUuid) {
        if(!isLocked() && !isActive()){
            slot.setupIfNeeded();
            activeProperty.setValue(true);
            if (services != null && services.getPersistenceService() != null) {
                UserProfile profil = services.getPersistenceService().getUserProfile(profileUuid);
                if (profil != null) {
                    nameProperty.setValue(profil.name());
                } else {
                    nameProperty.setValue("Joueur " + (index+1));
                }
            } else {
                nameProperty.setValue("Joueur " + (index+1));
            }
            slot.open(profileUuid);
        }
    }

    public void addPlayer(UserProfile profile) {
        if (!isLocked() && !isActive()) {
            slot.setupIfNeeded();
            activeProperty.setValue(true);
            nameProperty.setValue(profile.name());
            slot.open(profile.uuid());
        }
    }

    public void removePlayer() {
        if(!lockedProperty.getValue()){
            activeProperty.setValue(false);
            nameProperty.setValue("Indisponible");
            slot.close();
        }
    }

}
