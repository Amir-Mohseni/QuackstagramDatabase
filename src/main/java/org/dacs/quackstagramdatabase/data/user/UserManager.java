package org.dacs.quackstagramdatabase.data.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.data.post.Post;
import org.dacs.quackstagramdatabase.data.post.PostManager;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.CredentialEntity;
import org.dacs.quackstagramdatabase.database.entities.FollowsEntity;
import org.dacs.quackstagramdatabase.database.entities.PostEntity;
import org.dacs.quackstagramdatabase.database.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class UserManager {
    private EntityManager entityManager;
    private PostManager postManager;

    private User currentUser;

    @Autowired
    public UserManager(EntityManager entityManager, PostManager postManager) {
        this.entityManager = entityManager;
        this.postManager = postManager;
    }

    public List<User> getFollowers(User user) {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return follows.stream()
                    .filter(follow -> follow.getFollowed().equals(user.getUsername()))
                    .map(follow -> this.getByUsername(follow.getFollower()))
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getFollowing(User user) {
        try {
            EntityManager em = this.entityManager;

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return follows.stream()
                    .filter(follow -> follow.getFollower().equals(user.getUsername()))
                    .map(follow -> this.getByUsername(follow.getFollowed()))
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    public List<Post> getPostedPosts(User user) {
        try {
            EntityManager em = this.entityManager;

            List<PostEntity> postEntities = em.findAll(PostEntity.class);
            List<Post> posts = new ArrayList<>();

            for (PostEntity postEntity : postEntities) {
                if (postEntity.getUsername().equals(user.getUsername())) {
                    posts.add(new Post(postEntity.getPostId(), postEntity.getUsername(), postEntity.getCaption(), postEntity.getMediaUrl()));
                }
            }
            return posts;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
//        return rawPosts.stream()
//                .map(pictureUuid -> Handler.getDataManager().forPosts().getByUUID(UUID.fromString(pictureUuid)))
//                .collect(Collectors.toList());
    }

    public int getPostsCount(User user){
        return getPostedPosts(user).size();
    }

    public HashMap<User, LocalDateTime> getNotificationsSorted(User user){
        List<Map.Entry<User, LocalDateTime>> entryList = new ArrayList<>();

        for(Post picture : getPostedPosts(user)){
            entryList.addAll(this.postManager.getLikesData(picture).entrySet());
        }

        entryList.sort((e1, e2) -> {
            return e2.getValue().compareTo(e1.getValue()); //reverse order from the latest to earliest
        });

        HashMap<User, LocalDateTime> map = new HashMap<>();
        for(Map.Entry<User, LocalDateTime> entry : entryList)
            map.put(entry.getKey(), entry.getValue());

        return map;
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
