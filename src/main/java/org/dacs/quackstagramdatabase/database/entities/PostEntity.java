package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;
import org.dacs.quackstagramdatabase.database.annotations.Incremented;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(tableName = "Posts")
public class PostEntity {
    @Id
    @Incremented
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
