package org.dacs.quackstagramdatabase.ui.type;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.search.SearchBar;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.ui.UIUtil;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class SearchUI extends JFrame {
    private final UIUtil uiUtil;
    private final SearchBar searchBar;
    private final InstagramProfileUI profileUI;
    private final UIManager uiManager;

    public SearchUI(UIUtil uiUtil, SearchBar searchBar, InstagramProfileUI profileUI, UIManager uiManager) {
        this.uiUtil = uiUtil;
        this.uiManager = uiManager;
        setTitle("Search");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();

        this.searchBar = searchBar;
        this.profileUI = profileUI;
    }

    private void initializeUI() {

        getContentPane().removeAll(); // Clear existing components
        setLayout(new BorderLayout()); // Reset the layout manager

        JPanel headerPanel = createHeaderPanel(); // Method from your main.ui.type.InstagramProfileUI class
        JPanel navigationPanel = uiUtil.createNavigationPanel(uiManager); // Method from your main.ui.type.InstagramProfileUI class
        JPanel mainContentPanel = createMainContentPanel();

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();


    }

    private JPanel createMainContentPanel() {
        // Search Panel that gets the query in panel and returns the results below it
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        //Add a search button to the search panel next to the search field
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton, BorderLayout.EAST);

        // User result grid panel
        JPanel userGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userGridPanel.setBackground(Color.WHITE); // Set the background color to white
        userGridPanel.setPreferredSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT - 100)); // Set the size of the panel

        // Add the search panel and image grid panel to the main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(searchPanel, BorderLayout.NORTH);
        mainContentPanel.add(userGridPanel, BorderLayout.CENTER);

        // Add a mouse listener to the search button
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String query = searchField.getText();
                displaySearchResults(query, userGridPanel);
            }
        });

        return mainContentPanel;
    }

    private void displaySearchResults(String query, JPanel resultPanel) {
        // Clear the resultPanel before adding new search results
        resultPanel.removeAll();
        resultPanel.revalidate();
        resultPanel.repaint();

        List<User> results = searchBar.search(query);

        if (results.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No results found");
            resultPanel.add(noResultsLabel);
        } else {
            for (User user : results) {
                // Create a panel for each user
                JPanel userPanel = new JPanel(new BorderLayout());
                // Each line in the user panel will contain the user's username and profile pic
                JLabel profilePicLabel = new JLabel(user.getProfilePicture(50, 50));
                userPanel.add(profilePicLabel, BorderLayout.WEST);
                JLabel usernameLabel = new JLabel(user.getUsername());
                userPanel.add(usernameLabel, BorderLayout.CENTER);
                resultPanel.add(userPanel);

                // Add a mouse listener to the user panel
                userPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        //Open the profile of the user when the user panel is clicked
                        profileUI.setCurrentUser(user);
                        profileUI.setVisible(true);
                        uiManager.setCurrentFrame(profileUI);
                        dispose(); // Close the current frame
                    }
                });
            }
        }

        // After adding all users, revalidate and repaint the resultPanel
        resultPanel.revalidate();
        resultPanel.repaint();
    }



    private JPanel createHeaderPanel() {

        // Header Panel (reuse from main.ui.type.InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(" Search \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }
}
