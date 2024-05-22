package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

@Data
@Entity( tableName = "Credentials")
public class CredentialEntity {
    @Id
    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String passwordHash;
}
