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

    private static final String TAG = "GsonDeserializer";

    @Override
    public List<News> deserialize(final JsonElement json,
                                  final Type typeOfT,
                                  final JsonDeserializationContext context) throws JsonParseException {
        List<News> newsList = new ArrayList<>();

        JsonObject rootObject = json.getAsJsonObject();
        JsonArray jsonArray = rootObject.get("data").getAsJsonArray();

        for (JsonElement elem : jsonArray) {
            JsonObject obj = elem.getAsJsonObject();

            JsonElement titleObj = obj.get("title");
            String title = titleObj != null ? titleObj.getAsString() : null;

            JsonElement descriptionObj = obj.get("description");
            String description = descriptionObj != null ? descriptionObj.getAsString() : null;

            JsonElement urlObj = obj.get("url");
            String url = urlObj != null ? urlObj.getAsString() : null;

            JsonElement imageUrlObj = obj.get("image_url");
            String imageUrl = imageUrlObj != null ? imageUrlObj.getAsString() : null;

            JsonElement highlightObj = obj.get("highlight");
            boolean highlight = highlightObj != null && highlightObj.getAsBoolean();

            newsList.add(new News(UUID.randomUUID().toString(), title, description, url, imageUrl, highlight, false));
        }

        return newsList;
    }
}
