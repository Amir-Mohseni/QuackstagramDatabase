package org.dacs.quackstagramdatabase.ui;


import lombok.Getter;
import org.dacs.quackstagramdatabase.ui.type.*;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

public enum UI {

    EXPLORE(ExploreUI.class, "explore"),
    IMAGE_UPLOAD(ImageUploadUI.class, "add"),
    INSTAGRAM_PROFILE(InstagramProfileUI.class, "profile"),
    NOTIFICATIONS(NotificationsUI.class, "notification"),
    SIGN_IN(SignInUI.class, "sign_in"),
    SIGN_UP(SignUpUI.class, "sign_up"),
    HOME(QuakstagramHomeUI.class, "home"),
    SEARCH(SearchUI.class, "search");


    private final Class<? extends JFrame> clazz;
    @Getter
    public final String reference;

    UI(Class<? extends JFrame> clazz, String reference){
        this.clazz = clazz;
        this.reference = reference;
    }

    public JFrame newInstance(){
        AtomicReference<JFrame> newClass = new AtomicReference<>();
        try {
            newClass.set(clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return newClass.get();
    }

    public static UI getByReference(String reference){
        for(UI ui : UI.values())
            if(ui.getReference().equals(reference))
                return ui;

        return null;
    }
}
