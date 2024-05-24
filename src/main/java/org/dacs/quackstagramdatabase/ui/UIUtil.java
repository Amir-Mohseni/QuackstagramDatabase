package org.dacs.quackstagramdatabase.ui;


import org.dacs.quackstagramdatabase.Handler;
import org.springframework.stereotype.Component;

import javax.swing.*;
import org.dacs.quackstagramdatabase.ui.UIManager;
import java.awt.*;
import java.util.Objects;

@Component
public class UIUtil {

    public static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;

    public UIUtil() {
    }

    public JPanel createNavigationPanel(UIManager uiManager) {
        // Create and return the navigation panel
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton(uiManager,"img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(uiManager, "img/icons/search.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(uiManager, "img/icons/add.png", "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(uiManager, "img/icons/heart.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(uiManager, "img/icons/profile.png", "profile"));

        return navigationPanel;
    }

    private JButton createIconButton(UIManager uiManager, String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        button.addActionListener(e -> uiManager.display(Objects.requireNonNull(UI.getByReference(buttonType))));

        return button;
    }
}
