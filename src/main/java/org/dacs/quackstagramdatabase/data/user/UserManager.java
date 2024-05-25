package org.dacs.quackstagramdatabase.data.user;
import lombok.Data;
import org.dacs.quackstagramdatabase.Handler;
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
            List<FollowsEntity> follows = entityManager.findAll(FollowsEntity.class);

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
            List<FollowsEntity> follows = entityManager.findAll(FollowsEntity.class);

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
            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, primaryKeys);
            UserEntity userEntity = entityManager.find(UserEntity.class, primaryKeys);

            if (userEntity == null || !Handler.getUtil().matches(password, credentialEntity.getPasswordHash())) {
                setCurrentUser(null);
                return null;
            }

            User authenticatedUser = new User(userEntity.getUsername(), credentialEntity.getPasswordHash(), userEntity.getBio(), userEntity.getProfilePicture());

            setCurrentUser(authenticatedUser);
            return authenticatedUser;
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(String username) {
        try {
            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            return entityManager.find(UserEntity.class, primaryKeys) != null;
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> getPostedPosts(User user) {
        try {
            List<PostEntity> postEntities = entityManager.findAll(PostEntity.class);
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
            UserEntity userEntity = new UserEntity(username, pfp_extention, bio);
            String hashedPassword = Handler.getUtil().hash(password);
            CredentialEntity credentialEntity = new CredentialEntity(username, hashedPassword);

            entityManager.persist(userEntity);
            entityManager.persist(credentialEntity);

            return new User(username, hashedPassword, bio, pfp_extention);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getFollowersCount(User user){
        try {
            UserEntity userEntity = entityManager.find(UserEntity.class, Arrays.asList(user.getUsername()));
            return userEntity.getNumberOfFollowers();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getFollowingCount(User user) {
        try {
            UserEntity userEntity = entityManager.find(UserEntity.class, Arrays.asList(user.getUsername()));
            return userEntity.getNumberOfFollowing();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void follow(User follower, User followee){
        try {
            FollowsEntity newFollow = new FollowsEntity(follower.getUsername(), followee.getUsername());

            entityManager.persist(newFollow);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFollowing(User follower, User followee) {
        try {
            FollowsEntity follow = entityManager.find(FollowsEntity.class, Arrays.asList(follower.getUsername(), followee.getUsername()));

            return follow != null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getByUsername(String username) {
        try {
            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(username);

            UserEntity userEntity = entityManager.find(UserEntity.class, primaryKeys);
            CredentialEntity credentialEntity = entityManager.find(CredentialEntity.class, primaryKeys);

            return new User(userEntity.getUsername(), credentialEntity.getPasswordHash(), userEntity.getBio(), userEntity.getProfilePicture());
        }
        catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
