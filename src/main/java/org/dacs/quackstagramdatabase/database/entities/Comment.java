package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.util.UUID;

@Data
@Entity(tableName = "comments")
public class Comment {

    @Id
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "username")
    private String username;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "comment_date")
    private String commentDate;
}
