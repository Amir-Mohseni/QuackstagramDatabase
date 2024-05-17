package org.dacs.quackstagramdatabase.ui;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.ui.type.SignInUI;

import javax.swing.*;

public class UIManager {

    @Getter
    @Setter
    public JFrame currentFrame;

    public void startApp(){
        SignInUI frame = new SignInUI();
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
