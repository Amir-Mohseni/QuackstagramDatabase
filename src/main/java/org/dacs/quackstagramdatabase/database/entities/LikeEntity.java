package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(tableName = "Likes")
@NoArgsConstructor
public class LikeEntity {

    public LikeEntity(Integer postId, String username) {
        this.postId = postId;
        this.username = username;
    }

    @Id
    @Column(name = "PostID")
    private Integer postId;

    @Id
    @Column(name = "Username")
    private String username;

    @Column(name = "LikeDate")
    @Defaulted
    private Timestamp likeTimestamp;
}
