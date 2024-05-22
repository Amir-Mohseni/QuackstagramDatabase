package org.dacs.quackstagramdatabase.database.entities;

import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

@Entity(tableName = "Follows")
public class FollowsEntity {
    @Id
    @Column(name = "FollowerUsername")
    String follower;

    @Id
    @Column(name = "FollowedUsername")
    String followed;
}
