package org.dacs.quackstagramdatabase.data.picture;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Represents a picture on Quackstagram
public class Picture{

    @Getter
    private UUID uuid;
    @Getter
    @Setter
    private UUID postedByUuid;
    @Getter
    private String caption;
    @Getter
    private List<String> rawLikes;
    @Getter
    private Map<String, String> rawComments;
    @Getter
    @Setter
    private Map<String, String> rawLikesData;
    @Getter
    @Setter
    private String timePosted, extension;



    public Picture(UUID uuid, UUID postedByUuid, String caption, String extension, List<String> rawLikes, Map<String, String> rawComments){
        this.uuid = uuid;
        this.postedByUuid = postedByUuid;
        this.caption = caption;
        this.rawLikes = rawLikes;
        this.extension = extension;
        this.rawComments = rawComments;
    }

    public Picture uploadImage(File file){
        if(!Handler.getUtil().isPhoto(file))
            return null;

        setExtension(Handler.getUtil().getFileExtension(file));
        try {
            Files.copy(file.toPath(), Paths.get("img", "uploaded", uuid.toString() + "." + getExtension()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }


    public ImageIcon getImage(int width, int height){
        File path = Paths.get("img", "uploaded", uuid.toString() + "." + getExtension()).toFile();
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

    public List<User> getLikes(){
        return rawLikes.stream()
                .map(userUuid -> Handler.getDataManager().forUsers().getByUUID(UUID.fromString(userUuid)))
                .collect(Collectors.toList());
    }

    public HashMap<User, LocalDateTime> getLikesData(){
        HashMap<User, LocalDateTime> map = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for(Map.Entry<String, String> entry : rawLikesData.entrySet())
            map.put(Handler.getDataManager().forUsers().getByUUID(
                    UUID.fromString(entry.getKey())), LocalDateTime.parse(entry.getValue(), formatter));

        return map;
    }

    public HashMap<User, String> getComments(){
        HashMap<User, String> map = new HashMap<>();

        for(Map.Entry<String, String> entry : rawComments.entrySet())
            map.put(Handler.getDataManager().forUsers().getByUUID(UUID.fromString(entry.getKey())), entry.getValue());

        return map;
    }

    public LocalDateTime getWhenPosted(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timePosted, formatter);
    }

    public User getPostedBy(){
        return Handler.getDataManager().forUsers().getByUUID(getPostedByUuid());
    }

    public void addComment(User user, String comment) {
        rawComments.put(user.getUuid().toString(), comment);
        Handler.getDataManager().forPictures().save();
    }

    public boolean hasLiked(User user) {
        return rawLikes.contains(user.getUuid().toString());
    }

    public void addLike(User user) {
        if(hasLiked(user))
            return;
        rawLikes.add(user.getUuid().toString());
        rawLikesData.put(user.getUuid().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Handler.getDataManager().forPictures().save();
    }

}
