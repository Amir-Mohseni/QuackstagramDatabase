package org.dacs.quackstagramdatabase.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

@Setter
@Getter
@Entity(tableName = "Follows")
public class FollowsEntity {
    @Id
    @Column(name = "FollowerUsername")
    String follower;

    @Id
    @Column(name = "FollowedUsername")
    String followed;

    public FollowsEntity(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
    }
}
