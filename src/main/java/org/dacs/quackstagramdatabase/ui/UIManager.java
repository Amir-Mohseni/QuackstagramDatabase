package org.dacs.quackstagramdatabase.ui;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.ui.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class UIManager {

    @Getter
    @Setter
    public JFrame currentFrame;

    private SignInUI signInUI;

    private ApplicationContext applicationContext;

    public UIManager(SignInUI signInUI, ApplicationContext applicationContext) {
        signInUI.setUiManager(this);
        this.signInUI = signInUI;
        this.applicationContext = applicationContext;
    }

    public void intializeProfileUI(InstagramProfileUI profileUI) {
        signInUI.initializeUI(profileUI);
    }

    public void startApp(){
        SignInUI frame = signInUI;
        setCurrentFrame(frame);

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    public void display(String reference){
//        currentFrame.dispose();
        currentFrame.setVisible(false);
        JFrame newJFrame = switch (reference) {
            case "explore" -> this.applicationContext.getBean(ExploreUI.class);
            case "add" -> this.applicationContext.getBean(ImageUploadUI.class);
            case "profile" -> this.applicationContext.getBean(InstagramProfileUI.class);
            case "notification" -> this.applicationContext.getBean(NotificationsUI.class);
            case "sign_in" -> this.applicationContext.getBean(SignInUI.class);
            case "sign_up" -> this.applicationContext.getBean(SignUpUI.class);
            case "home" -> this.applicationContext.getBean(QuakstagramHomeUI.class);
            case "search" -> this.applicationContext.getBean(SearchUI.class);
            default -> null;
        };

        SwingUtilities.invokeLater(() -> {
            
//            JFrame newJFrame = ui.newInstance();
            newJFrame.setVisible(true);
            setCurrentFrame(newJFrame);
        });
    }


}
