package org.dacs.quackstagramdatabase.data.post;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.Comment;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.database.*;
import org.dacs.quackstagramdatabase.database.entities.CommentEntity;
import org.dacs.quackstagramdatabase.database.entities.LikeEntity;
import org.dacs.quackstagramdatabase.database.entities.PostEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


// Represents a picture on Quackstagram
public class Post {

    @Getter
    private Integer postID;
    @Getter
    @Setter
    private String postedByUsername;
    @Getter
    private String caption;
    @Getter
    @Setter
    private String timePosted, extension;



    public Post(Integer postID, String postedByUsername, String caption, String extension){
        this.postID = postID;
        this.postedByUsername = postedByUsername;
        this.caption = caption;
        this.extension = extension;
    }

    public Post uploadImage(File file){
        if(!Handler.getUtil().isPhoto(file))
            return null;

        setExtension(Handler.getUtil().getFileExtension(file));
        try {
            Files.copy(file.toPath(), Paths.get("img", "uploaded", postID.toString() + "." + getExtension()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }


    public ImageIcon getImage(int width, int height){
        File path = Paths.get("img", "uploaded", postID.toString() + "." + getExtension()).toFile();
        ImageIcon imageIcon;
        try {
            BufferedImage originalImage = ImageIO.read(path);
            Image croppedImage = originalImage
                    .getScaledInstance(
                            Math.min(originalImage.getWidth(), width),
                            Math.min(originalImage.getHeight(), height),
                            Image.SCALE_DEFAULT);
            imageIcon = new ImageIcon(croppedImage);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return imageIcon;
    }

    public List<CommentEntity> getComments(){
        List <CommentEntity> comments = new ArrayList<>();

        try {

            List<CommentEntity> allComments = entityManger.findAll(CommentEntity.class);
            for (CommentEntity comment : allComments) {
                if (comment.getPostId().equals(postID)) {
                    comments.add(comment);
                }
            }

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public LocalDateTime getWhenPosted() {
        try {
            List<Object> primaryKeys = new ArrayList<>();
            primaryKeys.add(postID);

            PostEntity postEntity = entityManger.find(PostEntity.class, primaryKeys);
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

    public int getLikesCount() {
        try {
            PostEntity postEntity = entityManger.find(PostEntity.class, Arrays.asList(postID));
            return postEntity.getNumberOfLikes();

        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void addComment(User user, String commentText) {
        try {
            CommentEntity commentEntity = new CommentEntity(postID, user.getUsername(), commentText);

            entityManger.persist(commentEntity);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasLiked(User user) {
        try {
            return entityManger.find(LikeEntity.class, Arrays.asList(postID, user.getUsername())) != null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addLike(User user) {
        if(hasLiked(user))
            return;
        try {
            LikeEntity likeEntity = new LikeEntity(postID, user.getUsername());

            entityManger.persist(likeEntity);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
