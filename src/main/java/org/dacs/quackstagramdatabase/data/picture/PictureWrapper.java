package org.dacs.quackstagramdatabase.data.picture;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PictureWrapper implements JsonDeserializer<Picture>, JsonSerializer<Picture>{

    @Override
    public JsonElement serialize(Picture src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("uuid", new JsonPrimitive(src.getUuid().toString()));
        result.add("posted_by_uuid", new JsonPrimitive(src.getPostedByUuid().toString()));
        result.add("caption", new JsonPrimitive(src.getCaption()));
        result.add("likes", context.serialize(src.getRawLikes()));
        result.add("comments", context.serialize(src.getRawComments()));
        result.add("likes_data", context.serialize(src.getRawLikesData()));
        result.add("time_posted", new JsonPrimitive(src.getTimePosted()));
        result.add("extension", new JsonPrimitive(src.getExtension()));

        return result;
    }

    @Override
    public Picture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        UUID postedByUuid = UUID.fromString(object.get("posted_by_uuid").getAsString());
        String caption = object.get("caption").getAsString();
        String extension = object.get("extension").getAsString();
        List<String> likes = context.deserialize(object.get("likes"), List.class);
        Map<String, String> comments = context.deserialize(object.get("comments"), HashMap.class);

        Picture picture = new Picture(uuid, postedByUuid, caption, extension, likes, comments);
        picture.setRawLikesData(context.deserialize(object.get("likes_data"), HashMap.class));
        picture.setTimePosted(object.get("time_posted").getAsString());

        return picture;
    }
}
