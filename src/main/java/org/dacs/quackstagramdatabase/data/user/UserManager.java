package org.dacs.quackstagramdatabase.data.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private String fileName = "users.json";
    private File file;
    private HashMap<UUID, User> users;

    @Getter
    @Setter
    private User currentUser;
    private Gson gson;

    public UserManager(){
        this.users = new HashMap<>();

        //loading all the users from the data file
        this.gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserWrapper())
                .setPrettyPrinting()
                .create();


        this.file = Paths.get("", fileName).toFile();
        if(containsSaves())
            load();
    }

    public User auth(String username, String password){
        User authenticatedUser = users.values().stream()
                .filter(user -> Handler.getUtil().matches(password, user.getHashedPassword()) && user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        setCurrentUser(authenticatedUser);
        return authenticatedUser;
    }

    public boolean exists(String username){
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    public void registerUser(User user){
        users.put(user.getUuid(), user);
        save();
    }

    public User getByUUID(UUID uuid){
        return users.get(uuid);
    }

    public List<User> getAsList(){
        return new ArrayList<>(users.values());
    }

    private boolean containsSaves(){
        return file.exists();
    }

    public void save(){
        try {
            if(!containsSaves())
                Files.writeString(file.toPath(), gson.toJson(getAsList()), StandardOpenOption.CREATE);
            else Files.writeString(file.toPath(), gson.toJson(getAsList()), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load(){

        List<User> loadedUsers;
        
        try(FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<User>>() {}.getType();

            loadedUsers = gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(User user : loadedUsers){
            users.put(user.getUuid(), user);
        }
    }
}
