package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

import java.sql.Timestamp;

@Data
@Entity(tableName = "Follows")
public class FollowsEntity {
    @Id
    @Column(name = "FollowerUsername")
    private String follower;

    @Id
    @Column(name = "FollowedUsername")
    private String followed;

    @Column(name = "FollowDate")
    @Defaulted
    private Timestamp followDate;

    public FollowsEntity(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
    }
}
