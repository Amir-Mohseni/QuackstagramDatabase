package org.dacs.quackstagramdatabase.data.post;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class PostManager {
    private EntityManager entityManager;
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
