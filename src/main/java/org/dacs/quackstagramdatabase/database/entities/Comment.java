package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class Comment {
    private UUID commentId;
    private UUID postId;
    private String username;
    private String commentText;
    private String commentDate;
}
