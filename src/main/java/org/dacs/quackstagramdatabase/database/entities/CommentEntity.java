package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;

import java.sql.Timestamp;

@Data
@Entity(tableName = "Comments")
@NoArgsConstructor
public class CommentEntity {

    public CommentEntity(Integer postId, String username, String commentText) {
        this.postId = postId;
        this.username = username;
        this.commentText = commentText;
    }

    @Id
    @Defaulted
    @Column(name = "CommentID")
    private Integer commentId;

    @Column(name = "PostID")
    private Integer postId;

    @Column(name = "Username")
    private String username;

    @Column(name = "CommentText")
    private String commentText;

    @Column(name = "CommentDate")
    @Defaulted
    private Timestamp commentDate;
}
