package org.dacs.quackstagramdatabase.ui;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.ui.type.InstagramProfileUI;
import org.dacs.quackstagramdatabase.ui.type.SignInUI;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class UIManager {
    private final SignInUI signInUI;

    @Getter
    @Setter
    public JFrame currentFrame;

    public UIManager(SignInUI signInUI) {
        this.signInUI = signInUI;
        signInUI.setUiManager(this);
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

    public void display(UI ui){
        currentFrame.dispose();
        SwingUtilities.invokeLater(() -> {
            
            JFrame newJFrame = ui.newInstance();
            newJFrame.setVisible(true);

            setCurrentFrame(newJFrame);
        });
    }


}
