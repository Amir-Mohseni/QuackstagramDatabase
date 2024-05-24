package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.sql.*;
import java.util.UUID;

@Data
@Entity(tableName = "Users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "Username")
    private String username;

    @Column(name = "ProfilePicture")
    private String profilePicture;

    @Column(name = "Bio")
    private String bio;

    @Column(name = "NumFollowers")
    @Defaulted
    private Integer numberOfFollowers;

    @Column(name = "NumFollowing")
    @Defaulted
    private Integer numberOfFollowing;

    @Column(name = "PostsCount")
    @Defaulted
    private Integer postsCount;

    public UserEntity(String username, String profilePicture, String bio) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }
}
