package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

@Data
@Entity( tableName = "credentials")
public class Credential {
    @Id
    private String username;
    private String passwordHash;
}
