package org.dacs.quackstagramdatabase.data.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.CredentialEntity;
import org.dacs.quackstagramdatabase.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class UserManager {
    private EntityManager entityManager;

    @Autowired
    public UserManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Getter
    @Setter
    private User currentUser;

    public User auth(String username, String password) {
        try {
            EntityManager entityManager = this.entityManager;

            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            User authenticatedUser = entityManager.find(User.class, primaryKeys);
            if (authenticatedUser == null || !Handler.getUtil().matches(password, authenticatedUser.getHashedPassword())) {
                authenticatedUser = null;
            }

            setCurrentUser(authenticatedUser);
            return authenticatedUser;
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(String username) {
        try {
            DatabaseConfig dbConfig = new DatabaseConfig();
            EntityManager entityManager = new EntityManager(dbConfig);

            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            return entityManager.find(User.class, primaryKeys) != null;
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public User registerUser(String username, String password, String bio, String pfp_extention) {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());
            UserEntity userEntity = new UserEntity(username, pfp_extention, bio);
            CredentialEntity credentialEntity = new CredentialEntity(username, password);

            em.persist(userEntity);
            em.persist(credentialEntity);

            return new User(username, password, bio, pfp_extention);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public User getByUsername(String username) {
        try {
            DatabaseConfig dbConfig = new DatabaseConfig();
            EntityManager entityManager = new EntityManager(dbConfig);

            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            return entityManager.find(User.class, primaryKeys);
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
