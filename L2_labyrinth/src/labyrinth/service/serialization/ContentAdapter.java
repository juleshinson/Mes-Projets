package labyrinth.service.serialization;

import com.google.gson.*;
import labyrinth.model.Content;
import labyrinth.model.None;
import labyrinth.model.StartingPosition;
import labyrinth.model.Treasure;

import java.lang.reflect.Type;

public class ContentAdapter implements JsonSerializer<Content>, JsonDeserializer<Content> {

    @Override
    public JsonElement serialize(Content content, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        switch (content) {
            case None none -> object.addProperty("type", "none");
            case Treasure treasure -> {
                object.addProperty("type", "treasure");
                object.add("value", jsonSerializationContext.serialize(treasure));
            }
            case StartingPosition startingPosition -> {
                object.addProperty("type", "startingPosition");
                object.add("value", jsonSerializationContext.serialize(startingPosition));
            }
            case null, default -> throw new JsonParseException("Content inconu");
        }

        return object ;


    }
    @Override
    public Content deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String kind = obj.get("type").getAsString();

        return switch (kind) {
            case "none" -> new None();

            case "treasure" -> jsonDeserializationContext.deserialize(obj.get("value"), Treasure.class);

            case "startingPosition" -> jsonDeserializationContext.deserialize(obj.get("value"), StartingPosition.class);

            default -> throw new JsonParseException("Content inconnu: " + type);
        };

    }


}
