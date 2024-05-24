package org.dacs.quackstagramdatabase.data.post;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.LikeEntity;
import org.dacs.quackstagramdatabase.database.entities.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class PostManager {
    private EntityManager entityManager;
    private DataManager dataManager;
    private HashMap<Integer, Post> posts;

    @Autowired
    public PostManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.posts = new HashMap<>();
    }

    public void postPost(User user, Post post){
        posts.put(post.getPostID(), post);
        post.setPostedByUsername(user.getUsername());

        try {
            entityManager.persist(new PostEntity(user.getUsername(), post.getCaption(), post.getExtension()));

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Post getByPostID(Integer postID){
        return posts.get(postID);
    }

    public List<Post> getAsList(){
        return new ArrayList<>(posts.values());
    }


    public List<User> getLikes(Post post) {
        List<User> likes = new ArrayList<>();
        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<LikeEntity> allLikes = em.findAll(LikeEntity.class);
            for (LikeEntity like : allLikes) {
                if (like.getPostId().equals(post.getPostID())) {
                    likes.add(this.dataManager.forUsers().getByUsername(like.getUsername()));
                }
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return likes;
    }

    public User getPostedBy(Post post) {
        return this.dataManager.forUsers().getByUsername(post.getPostedByUsername());
    }

    public HashMap<User, LocalDateTime> getLikesData(Post post){
        HashMap<User, LocalDateTime> map = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            EntityManager em = new EntityManager(new DatabaseConfig());

            List<LikeEntity> allLikes = em.findAll(LikeEntity.class);
            for (LikeEntity like : allLikes) {
                if (like.getPostId().equals(post.getPostID())) {
                    User user = this.dataManager.forUsers().getByUsername(like.getUsername());
                    Timestamp timestamp = like.getLikeTimestamp();
                    LocalDateTime likeTime = timestamp.toLocalDateTime();
                    map.put(user, likeTime);
                }
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

//    private void load(){
//        try {
//            EntityManager em = new EntityManager(new DatabaseConfig());
//
//            List<PostEntity> postEntities = em.findAll(PostEntity.class);
//
//            for (PostEntity postEntity : postEntities) {
//                Post post = new Post(postEntity.getPostId(), postEntity.getUsername(), postEntity.getCaption(), postEntity.getMediaUrl());
//                posts.put(post.getPostID(), post);
//            }
//        } catch (SQLException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
