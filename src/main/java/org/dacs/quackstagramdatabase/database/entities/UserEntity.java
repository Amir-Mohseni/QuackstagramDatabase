package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;

import java.sql.*;
import java.util.UUID;

@Data
@Entity(tableName = "Users")
public class UserEntity {
    @Column(name = "Username")
    private String username;

    @Column(name = "ProfilePicture")
    private String profilePicture;

    @Column(name = "Bio")
    private String bio;

    @Column(name = "NumFollowers")
    private Integer numberOfFollowers;

    @Column(name = "NumFollowing")
    private Integer numberOfFollowing;

    @Column(name = "PostsCount")
    private Integer postsCount;

    public UserEntity(String username, String profilePicture, String bio) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }
}
