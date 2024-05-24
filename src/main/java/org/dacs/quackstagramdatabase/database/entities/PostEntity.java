package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;

import java.sql.Timestamp;

@Data
@Entity(tableName = "Posts")
public class PostEntity {

    public PostEntity(String username, String caption, String mediaUrl) {
        this.username = username;
        this.caption = caption;
        this.mediaUrl = mediaUrl;
    }

    @Id
    @Defaulted
    @Column(name = "PostID")
    private Integer postId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Caption")
    private String caption;

    @Column(name = "MediaURL")
    private String mediaUrl;

    @Column(name = "PostDate")
    private Timestamp postTimestamp;

    @Column(name = "NumLikes")
    private Integer numberOfLikes;

    @Column(name = "NumComments")
    private Integer numberOfComments;
}
