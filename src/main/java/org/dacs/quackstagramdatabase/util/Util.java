package org.dacs.quackstagramdatabase.util;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.io.File;

public class Util {

    private Argon2 argon2;

    public Util(){
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    }

    public String hash(String password) {
        return argon2.hash(2, 15 * 1024, 1, password.toCharArray());
    }

    public boolean matches(String password, String hash){
        return argon2.verify(hash, password.toCharArray());
    }

    public String getFileExtension(File file){
        String fileName = file.toString();
        int index = fileName.lastIndexOf('.');

        return fileName.substring(index + 1).toLowerCase();
    }

    public boolean isPhoto(File file){
        String extension = getFileExtension(file);
        return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg");
    }
}
