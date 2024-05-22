package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(tableName = "Likes")
public class LikeEntity {
    @Id
    @Column(name = "PostID")
    private Integer postId;

    @Id
    @Column(name = "Username")
    private String username;
}
