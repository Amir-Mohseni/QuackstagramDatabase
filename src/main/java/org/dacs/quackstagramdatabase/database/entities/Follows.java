package org.dacs.quackstagramdatabase.database.entities;

import org.dacs.quackstagramdatabase.database.annotations.Entity;

@Entity(tableName = "FOLLOWS")
public class Follows {

    String follower;

    String followed;
}
