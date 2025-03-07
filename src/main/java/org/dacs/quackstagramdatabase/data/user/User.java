package org.dacs.quackstagramdatabase.data.user;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

    public ImageIcon getProfilePicture(int width, int height){
        return new ImageIcon(
                new ImageIcon(Paths.get("src/main/resources/img", "profile",  username + "." + getExtension()).toString())
                        .getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void setProfilePicture(File file) {
        if(!Handler.getUtil().isPhoto(file))
            return;

        setExtension(Handler.getUtil().getFileExtension(file));

        try {
            BufferedImage image = ImageIO.read(file);
            File outputDirectory = Paths.get("src/main/resources/img", "profile").toFile();
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs(); // Create the directory if it does not exist
            }
            File outputFile = Paths.get("src/main/resources/img", "profile",  username + "." + getExtension()).toFile();
            ImageIO.write(image, getExtension(), outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}