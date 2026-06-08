package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.service.persistence.UserProfile;

import java.util.UUID;

public interface PlayerViewModel {

    int getIndex();

    ObservableValue<Boolean> activeProperty();

    boolean isActive();

    ObservableValue<String> nameProperty();

    String getName();

    ObservableValue<Boolean>lockedProperty();

    boolean isLocked();

    void addPlayer();

    void addBot();

    void removePlayer();

    void setLockedProperty(Boolean locked);

    void addPlayer(UUID profileUuid);

    void addPlayer(UserProfile profile);
}
