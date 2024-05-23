package org.dacs.quackstagramdatabase.data.search;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.UserEntity;
import org.w3c.dom.UserDataHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBar {
    List<User> results;

    public List<User> search(String query){
        //search for users
        results = new ArrayList<>();
        UserManager userManager = Handler.getDataManager().forUsers();

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
            EntityManager em = new EntityManager(new DatabaseConfig());
            List <UserEntity> userEntities = em.findAll(UserEntity.class);
            List <User> users = new ArrayList<>();

            for (UserEntity userEntity : userEntities) {
                String username = userEntity.getUsername();
                users.add(Handler.getDataManager().forUsers().getByUsername(username));
            }

            return users;

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
