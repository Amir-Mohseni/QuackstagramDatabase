package org.dacs.quackstagramdatabase.data;

import org.dacs.quackstagramdatabase.data.post.PostManager;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataManager {

    private EntityManager entityManager;
    private PostManager postManager;
    private UserManager userManager;

    @Autowired
    public DataManager(EntityManager entityManager) {
        this.entityManager = entityManager;

        this.postManager = new PostManager(entityManager);
        this.userManager = new UserManager(entityManager);

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
