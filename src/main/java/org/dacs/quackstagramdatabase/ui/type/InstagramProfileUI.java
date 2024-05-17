package org.dacs.quackstagramdatabase.ui.type;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.picture.Picture;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.ui.UIUtil;

import javax.swing.*;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;



public class InstagramProfileUI extends JFrame {

    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = UIUtil.WIDTH / 3; // Static size for grid images
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel;   // Panel for the header
    private JPanel navigationPanel; // Panel for the navigation

    @Getter
    @Setter
    private User currentUser; // User object to store the current user's information

    public InstagramProfileUI() {

        //This is a workaround so the magnificent UI manager works properly
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            if(currentUser == null){
                currentUser = Handler.getDataManager().forUsers().getCurrentUser();
            }
            System.out.println("Bio for " + currentUser.getUsername() + ": " + currentUser.getBio());
            System.out.println(currentUser.getPostsCount());

            setTitle("DACS Profile");
            setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
            setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            contentPanel = new JPanel();
            headerPanel = createHeaderPanel();       // Initialize header panel
            navigationPanel = UIUtil.createNavigationPanel(); // Initialize navigation panel

            initializeUI();

        }, 100, TimeUnit.MILLISECONDS);


    }


    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        User loggedInUser = Handler.getDataManager().forUsers().getCurrentUser();
        boolean isCurrentUser = loggedInUser.getUuid().equals(currentUser.getUuid());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        JLabel profileImage = new JLabel(currentUser.getProfilePicture(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE));
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user" + currentUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getRawFollowers().size()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getRawFollowing().size()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding


        // Follow Button
        // Follow or Edit Profile Button
        // followButton.addActionListener(e -> handleFollowAction(currentUser.getUsername()));
        JButton followButton;
        if (isCurrentUser) {
            followButton = new JButton("Edit Profile");
        } else {
            followButton = new JButton("Follow");

            if(currentUser.getRawFollowers().contains(loggedInUser.getUuid().toString())) {
                followButton.setText("Following");
            } else {
                followButton.addActionListener(e -> {
                    loggedInUser.follow(currentUser);
                    followButton.setText("Following");

                    initializeUI();
                });
            }
        }

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding


        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(currentUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(currentUser.getBio());
        System.out.println("This is the bio " + currentUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);


        return headerPanel;

    }

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        for(Picture picture : currentUser.getPostedPictures()){
            JLabel imageLabel = new JLabel(picture.getImage(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE));
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(picture); // Call method to display the clicked image
                }
            });
            contentPanel.add(imageLabel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    private JPanel displayLikeCount(Picture picture) {
        JPanel likePanel = new JPanel();
        likePanel.setLayout(new BoxLayout(likePanel, BoxLayout.Y_AXIS));
        likePanel.setBackground(new Color(249, 249, 249));

        JLabel likesLabel = new JLabel("Likes: " + picture.getRawLikes().size());
        likesLabel.setFont(new Font("Arial", Font.BOLD, 12));
        likesLabel.setForeground(Color.BLACK);
        likePanel.add(likesLabel);

        return likePanel;

    }


    private void displayImage(Picture picture) {
        ImageIcon imageIcon = picture.getImage(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE);
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        //add like count
        contentPanel.add(displayLikeCount(picture), BorderLayout.NORTH);

        revalidate();
        repaint();
    }


    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

}
