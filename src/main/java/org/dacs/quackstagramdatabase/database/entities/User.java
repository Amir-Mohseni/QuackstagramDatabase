package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;

import java.sql.*;
import java.util.UUID;

@Data
public class User {
    private UUID commentId;
    private UUID postId;
    private String username;
    private String commentText;
    private Timestamp commentTimestamp;
}
