package labyrinth.service.serialization;

public interface SerializationService {

        String serialize(GameDTO game);
        GameDTO deserialize(String data);

}
