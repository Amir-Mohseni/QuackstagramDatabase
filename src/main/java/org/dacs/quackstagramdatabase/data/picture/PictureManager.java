package org.dacs.quackstagramdatabase.data.picture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PictureManager {


    private String fileName = "pictures.json";
    private File file;
    private HashMap<UUID, Picture> pictures;
    private Gson gson;

    public PictureManager(){
        this.pictures = new HashMap<>();

        //loading all the pictures from the data file
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Picture.class, new PictureWrapper())
                .setPrettyPrinting()
                .create();

        this.file = Paths.get("data", fileName).toFile();

        if(containsSaves())
            load();
    }

    public void postPicture(User user, Picture picture){
        pictures.put(picture.getUuid(), picture);
        picture.setPostedByUuid(user.getUuid());
        picture.setRawLikesData(new HashMap<>());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        picture.setTimePosted(LocalDateTime.now().format(formatter));

        user.addPicture(picture.getUuid());
        Handler.getDataManager().saveAll();
    }

    public Picture getByUUID(UUID uuid){
        return pictures.get(uuid);
    }

    public List<Picture> getAsList(){
        return new ArrayList<>(pictures.values());
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
        List<Picture> loadedPictures;
        try(FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Picture>>() {}.getType();

            loadedPictures = gson.fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(Picture picture : loadedPictures){
            pictures.put(picture.getUuid(), picture);
        }
    }
}
