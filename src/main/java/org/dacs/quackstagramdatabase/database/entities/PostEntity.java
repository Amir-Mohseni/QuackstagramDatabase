package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.sql.Timestamp;

@Data
@Entity(tableName = "Posts")
@NoArgsConstructor
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

    @Getter
    @Column(name = "Caption")
    private String caption;

    @Column(name = "MediaURL")
    private String mediaUrl;

    @Column(name = "PostDate")
    @Defaulted
    private Timestamp postTimestamp;

    @Column(name = "NumLikes")
    @Defaulted
    private Integer numberOfLikes;

    @Column(name = "NumComments")
    @Defaulted
    private Integer numberOfComments;

    public Integer getPostID() {
        return this.postId;
    }

    public String getPostedByUsername() {
        return this.username;
    }

    public String getExtension() {
        return this.mediaUrl;
    }

    public Integer getPostId() {
        return this.postId;
    }
}
