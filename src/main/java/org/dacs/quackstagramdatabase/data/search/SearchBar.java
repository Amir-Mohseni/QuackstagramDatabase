package org.dacs.quackstagramdatabase.data.search;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.UserDataHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchBar {
    private final UserManager userManager;
    private final EntityManager entityManager;

    List<User> results;

    @Autowired
    public SearchBar(UserManager userManager, EntityManager entityManager) {
        this.userManager = userManager;
        this.entityManager = entityManager;
    }

    public List<User> search(String query){
        //search for users
        results = new ArrayList<>();

        List <User> allUsers = getAllUsers();
        User currentUser = userManager.getCurrentUser();

        for (User user : allUsers) {
            if (currentUser.getUsername().equals(user.getUsername()))
                continue;
            if (user.getUsername().startsWith(query)) {
                results.add(user);
            }
        }

        return results;
    }

    public List<User> getAllUsers() {
        try {
            List <UserEntity> userEntities = entityManager.findAll(UserEntity.class);
            List <User> users = new ArrayList<>();

            for (UserEntity userEntity : userEntities) {
                String username = userEntity.getUsername();
                users.add(this.userManager.getByUsername(username));
            }

            return users;

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
