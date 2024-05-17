package org.dacs.quackstagramdatabase.data;

import lombok.Getter;
import org.dacs.quackstagramdatabase.data.picture.PictureManager;
import org.dacs.quackstagramdatabase.data.user.UserManager;

public class DataManager {

    private PictureManager pictureManager;
    private UserManager userManager;
    public DataManager() {
        this.pictureManager = new PictureManager();
        this.userManager = new UserManager();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveAll));
    }

    public void saveAll(){
        pictureManager.save();
        userManager.save();
    }

    public PictureManager forPictures(){
        return pictureManager;
    }

    public UserManager forUsers(){
        return userManager;
    }
}
