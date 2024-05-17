package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;

@Data
public class Credential {
    private String username;
    private String passwordHash;
}
