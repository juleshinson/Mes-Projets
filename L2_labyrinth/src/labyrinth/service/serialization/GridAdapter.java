package labyrinth.service.serialization;

import com.google.gson.*;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.lang.reflect.Type;

public class GridAdapter implements JsonSerializer<Grid<TileDTO>>,
        JsonDeserializer<Grid<TileDTO>> {
    @Override
    public JsonElement serialize(Grid<TileDTO> entries, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray tableau = new JsonArray();
        // avant on faisais tableau.size() mais ca vaut 0 au debut donc ca rentrait jamais dans la boucle...
        for(int i =0 ; i< entries.getWidth() ; i++){
            JsonArray ligne = new JsonArray();
            for(int j = 0 ; j< entries.getHeight(); j++){
                TileDTO tile = entries.get(i, j);
                ligne.add(jsonSerializationContext.serialize(tile));
            }
            tableau.add(ligne);
        }
        return tableau;
    }

    @Override
    public Grid<TileDTO> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray tableau = jsonElement.getAsJsonArray();
        int height = tableau.size();
        int width = tableau.get(0).getAsJsonArray().size();
        Grid<TileDTO> grid = new ArrayGrid<>(width,height);
        for(int j = 0 ; j< width ; j++){
            // ici il faut prendre tableau.get(i) pas juste tableau sinon on prend toujours la meme ligne
            JsonArray tab = tableau.get(j).getAsJsonArray();
            for(int i = 0 ; i< height; i++){
                TileDTO tile = jsonDeserializationContext.deserialize(tab.get(i),TileDTO.class);
                grid.set(i,j,tile);
            }
        }
        return grid;
    }
}
