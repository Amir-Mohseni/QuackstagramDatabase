package org.dacs.quackstagramdatabase.data.user;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class UserWrapper implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("uuid", new JsonPrimitive(src.getUuid().toString()));
        result.add("username", new JsonPrimitive(src.getUsername()));
        result.add("hashed_password", new JsonPrimitive(src.getHashedPassword()));
        result.add("followers", context.serialize(src.getRawFollowers()));
        result.add("following", context.serialize(src.getRawFollowing()));
        result.add("pictures", context.serialize(src.getRawPictures()));
        result.add("bio", new JsonPrimitive(src.getBio()));
        result.add("posts_count", new JsonPrimitive(src.getPostsCount()));
        result.add("pfp_extension", new JsonPrimitive(src.getExtension()));

        return result;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        String username = object.get("username").getAsString();
        String hashedPassword = object.get("hashed_password").getAsString();
        List<String> followers = context.deserialize(object.get("followers"), List.class);
        List<String> following = context.deserialize(object.get("following"), List.class);
        List<String> pictures = context.deserialize(object.get("pictures"), List.class);
        String bio = object.get("bio").getAsString();
        String extension = object.get("pfp_extension").getAsString();
        int postsCount = object.get("posts_count").getAsInt();

        return new User(uuid, username, hashedPassword, followers, following, pictures, bio, extension, postsCount);
    }
}
