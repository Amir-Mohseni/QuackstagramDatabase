package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Like {
    private UUID likeId;
    private UUID postId;
    private String username;
    private Timestamp likeTimestamp;
}
