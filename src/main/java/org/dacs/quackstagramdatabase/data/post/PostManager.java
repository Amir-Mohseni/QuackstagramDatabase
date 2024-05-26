package org.dacs.quackstagramdatabase.data.post;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.CommentEntity;
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
import java.util.*;

@Component
public class PostManager {
    private EntityManager entityManager;
    private HashMap<Integer, Post> posts;
    @Setter
    private UserManager userManager;

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
        try {
            List<Post> posts = new LinkedList<>();
            entityManager.findAll(PostEntity.class).forEach( p -> posts.add(new Post(p)));
            return posts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public List<User> getLikes(Post post) {
        List<User> likes = new ArrayList<>();
        try {
            List<LikeEntity> allLikes = entityManager.findAll(LikeEntity.class);
            for (LikeEntity like : allLikes) {
                if (like.getPostId().equals(post.getPostID())) {
                    likes.add(this.userManager.getByUsername(like.getUsername()));
                }
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return likes;
    }

    public User getPostedBy(Post post) {
        return this.userManager.getByUsername(post.getPostedByUsername());
    }


    public LocalDateTime getWhenPosted(Post post) {
        try {
            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(post.getPostID());

            PostEntity postEntity = entityManager.find(PostEntity.class, primaryKeys);
            if(postEntity == null) // Post not found
                return null;

            // Parse the timestamp using formater and LocalDateTime

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = postEntity.getPostTimestamp();

            return timestamp.toLocalDateTime(); // Convert to LocalDateTime
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return LocalDateTime.parse(timePosted, formatter);
    }

    public HashMap<User, LocalDateTime> getLikesData(Post post){
        HashMap<User, LocalDateTime> map = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<LikeEntity> allLikes = entityManager.findAll(LikeEntity.class);
            for (LikeEntity like : allLikes) {
                if (like.getPostId().equals(post.getPostID())) {
                    User user = this.userManager.getByUsername(like.getUsername());
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



    public List<CommentEntity> getComments(Post post){
        List <CommentEntity> comments = new ArrayList<>();

        try {

            List<CommentEntity> allComments = entityManager.findAll(CommentEntity.class);
            for (CommentEntity comment : allComments) {
                if (comment.getPostId().equals(post.getPostID())) {
                    comments.add(comment);
                }
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public int getLikesCount(Post post) {
        try {
            PostEntity postEntity = entityManager.find(PostEntity.class, Arrays.asList(post.getPostID()));
            return postEntity.getNumberOfLikes();

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void addComment(User user, String commentText, Post post) {
        try {
            CommentEntity commentEntity = new CommentEntity(post.getPostID(), user.getUsername(), commentText);

            entityManager.persist(commentEntity);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasLiked(User user, Post post) {
        try {
            return entityManager.find(LikeEntity.class, Arrays.asList(post.getPostID(), user.getUsername())) != null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLike(User user, Post post) {
        if(hasLiked(user, post))
            return;
        try {
            LikeEntity likeEntity = new LikeEntity(post.getPostID(), user.getUsername());

            entityManager.persist(likeEntity);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
