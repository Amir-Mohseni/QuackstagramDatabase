package org.dacs.quackstagramdatabase.data.post;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


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

        File outputDirectory = Paths.get("src/main/resources/img", "uploaded").toFile();
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs(); // Create the directory if it does not exist
        }

        try {
            Files.copy(file.toPath(), Paths.get("src/main/resources/img", "uploaded", postID.toString() + "." + getExtension()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }


    public ImageIcon getImage(int width, int height){
        File path = Paths.get("src/main/resources/img", "uploaded", postID.toString() + "." + getExtension()).toFile();
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
//            throw new RuntimeException(ex);
            return null;
        }

        return imageIcon;
    }

}
