package org.dacs.quackstagramdatabase.data;

import org.dacs.quackstagramdatabase.data.post.PostManager;
import org.dacs.quackstagramdatabase.data.user.UserManager;

public class DataManager {

    private PostManager postManager;
    private UserManager userManager;
    public DataManager() {
        this.postManager = new PostManager();
        this.userManager = new UserManager();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveAll));
    }

    public void saveAll(){
        // Save anything needed to be saved
    }

    public PostManager forPosts(){
        return postManager;
    }

    public UserManager forUsers(){
        return userManager;
    }
}
