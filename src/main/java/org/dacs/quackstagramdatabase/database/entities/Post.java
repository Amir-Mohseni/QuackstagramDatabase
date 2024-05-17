package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Post {
    private UUID postId;
    private String username;
    private String caption;
    private String mediaUrl;
    private Timestamp postTimestamp;
    private Integer numberOfLikes;
//    private
}
