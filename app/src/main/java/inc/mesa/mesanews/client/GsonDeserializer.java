package inc.mesa.mesanews.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import inc.mesa.mesanews.data.News;

public class GsonDeserializer implements JsonDeserializer<List<News>> {

    @Override
    public List<News> deserialize(final JsonElement json,
                                  final Type typeOfT,
                                  final JsonDeserializationContext context) throws JsonParseException {
        List<News> newsList = new ArrayList<>();

        JsonObject rootObject = json.getAsJsonObject();
        JsonArray jsonArray = rootObject.get("data").getAsJsonArray();

        for (JsonElement elem : jsonArray) {
            JsonObject obj = elem.getAsJsonObject();

            String title = obj.get("title").getAsString();
            String description = obj.get("description").getAsString();
            String url = obj.get("url").getAsString();
            String imageUrl = obj.get("image_url").getAsString();
            boolean highlight = obj.get("highlight").getAsBoolean();

            newsList.add(new News(UUID.randomUUID().toString(), title, description, url, imageUrl, highlight, false));
        }

        return newsList;
    }
}
