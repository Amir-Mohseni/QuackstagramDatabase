package org.dacs.quackstagramdatabase.data.user;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.post.Post;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.FollowsEntity;
import org.dacs.quackstagramdatabase.database.entities.PostEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Represents a user on Quackstagram
public class User  {
    // Getter methods for user details
    @Getter
    private String username;
    @Getter
    @Setter
    private String bio, extension;
    @Getter
    private String hashedPassword;



    public User(String username, String hashedPassword, String bio, String extension) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.bio = bio;
        this.extension = extension;
    }

    public void follow(User user){
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            FollowsEntity newFollow = new FollowsEntity(this.username, user.getUsername());

            em.persist(newFollow);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFollowing(User user) {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            FollowsEntity follow = em.find(FollowsEntity.class, Arrays.asList(this.username, user.getUsername()));

            return follow != null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> getPostedPosts() {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<PostEntity> postEntities = em.findAll(PostEntity.class);
            List<Post> posts = new ArrayList<>();

            for (PostEntity postEntity : postEntities) {
                if (postEntity.getUsername().equals(this.username)) {
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

    public HashMap<User, LocalDateTime> getNotificationsSorted(){
        List<Map.Entry<User, LocalDateTime>> entryList = new ArrayList<>();

        for(Post picture : getPostedPosts()){
            entryList.addAll(picture.getLikesData().entrySet());
        }

        entryList.sort((e1, e2) -> {
            return e2.getValue().compareTo(e1.getValue()); //reverse order from the latest to earliest
        });

        HashMap<User, LocalDateTime> map = new HashMap<>();
        for(Map.Entry<User, LocalDateTime> entry : entryList)
            map.put(entry.getKey(), entry.getValue());

        return map;
    }

    public List<User> getFollowing() {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return follows.stream()
                    .filter(follow -> follow.getFollower().equals(this.username))
                    .map(follow -> Handler.getDataManager().forUsers().getByUsername(follow.getFollowed()))
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getPostsCount(){
        return getPostedPosts().size();
    }

    public int getFollowingCount() {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return (int) follows.stream()
                    .filter(follow -> follow.getFollower().equals(this.username))
                    .count();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getFollowersCount(){
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return (int) follows.stream()
                    .filter(follow -> follow.getFollowed().equals(this.username))
                    .count();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon getProfilePicture(int width, int height){
        return new ImageIcon(
                new ImageIcon(Paths.get("img", "storage", "profile",  username + "." + getExtension()).toString())
                        .getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void setProfilePicture(File file) {
        if(!Handler.getUtil().isPhoto(file))
            return;

        setExtension(Handler.getUtil().getFileExtension(file));

        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = Paths.get("img", "storage", "profile",  username + "." + getExtension()).toFile();
            ImageIO.write(image, getExtension(), outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getFollowers() {
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<FollowsEntity> follows = em.findAll(FollowsEntity.class);

            return follows.stream()
                    .filter(follow -> follow.getFollowed().equals(this.username))
                    .map(follow -> Handler.getDataManager().forUsers().getByUsername(follow.getFollower()))
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}