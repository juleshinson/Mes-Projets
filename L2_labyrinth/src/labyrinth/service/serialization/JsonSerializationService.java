package labyrinth.service.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import labyrinth.model.Content;
import labyrinth.util.collection.Grid;

public class JsonSerializationService implements SerializationService{
    private Gson gson ;

    public JsonSerializationService(){
        this.gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Grid<TileDTO>>() {}.getType(), new GridAdapter())
                .registerTypeAdapter(Content.class, new ContentAdapter())
                .create();
    }
    @Override
    public String serialize(GameDTO game) {
        return gson.toJson(game);

    }

    @Override
    public GameDTO deserialize(String data) {
        return gson.fromJson(data,GameDTO.class);

    }
}
