package org.dacs.quackstagramdatabase.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;

@Data
@Entity( tableName = "Credentials")
@NoArgsConstructor
public class CredentialEntity {
    @Id
    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String passwordHash;

    public CredentialEntity(String username, String password) {
        this.username = username;
        this.passwordHash = password;
    }
}
