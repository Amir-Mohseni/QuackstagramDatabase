package org.dacs.quackstagramdatabase.data.search;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;

import java.util.ArrayList;
import java.util.List;

public class SearchBar {
    List<User> results;

    public List<User> search(String query){
        //search for users
        results = new ArrayList<>();

        List <User> users = Handler.getDataManager().forUsers().getAsList();
        User currentUser = Handler.getDataManager().forUsers().getCurrentUser();

        for (User user : users) {
            if (currentUser.getUsername().equals(user.getUsername()))
                continue;
            if (user.getUsername().startsWith(query)) {
                results.add(user);
            }
        }

        return results;
    }
}
