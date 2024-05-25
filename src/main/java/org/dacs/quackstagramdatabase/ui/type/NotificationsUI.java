package org.dacs.quackstagramdatabase.ui.type;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.ui.UIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

@Component
public class NotificationsUI extends JFrame {
    private final UIUtil uiUtil;
    private final UIManager uiManager;
    private UserManager userManager;

    @Autowired
    public NotificationsUI(UIUtil uiUtil, UIManager uiManager, UserManager userManager) {
        this.uiUtil = uiUtil;
        this.uiManager = uiManager;
        this.userManager = userManager;
        setTitle("Notifications");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Reuse the header and navigation panel creation methods from the main.ui.type.InstagramProfileUI class
        JPanel headerPanel = createHeaderPanel();
        JPanel navigationPanel = uiUtil.createNavigationPanel(uiManager);

        // Content Panel for notifications
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        User currentUser = this.userManager.getCurrentUser();
        if (currentUser != null) {
            for(Map.Entry<User, LocalDateTime> entry : this.userManager.getNotificationsSorted(currentUser).entrySet()){

                String notificationMessage = entry.getKey().getUsername() + " liked your picture - " + getElapsedTime(entry.getValue()) + " ago";

                // Add the notification to the panel
                JPanel notificationPanel = new JPanel(new BorderLayout());
                notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel notificationLabel = new JLabel(notificationMessage);
                notificationPanel.add(notificationLabel, BorderLayout.CENTER);

                // Add profile icon (if available) and timestamp
                // ... (Additional UI components if needed)

                contentPanel.add(notificationPanel);

            }
        }

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private String getElapsedTime(LocalDateTime timeOfNotification) {
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }
        return timeElapsed.toString();
    }

    private JPanel createHeaderPanel() {

        // Header Panel (reuse from main.ui.type.InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(" Notifications \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

}
