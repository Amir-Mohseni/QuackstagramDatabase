package org.dacs.quackstagramdatabase.data;

import lombok.Data;
import org.dacs.quackstagramdatabase.data.post.PostManager;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class DataManager {
    private PostManager postManager;
    private UserManager userManager;

    @Autowired
    public DataManager(PostManager postManager, UserManager userManager) {
        this.postManager = postManager;
        this.userManager = userManager;

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
