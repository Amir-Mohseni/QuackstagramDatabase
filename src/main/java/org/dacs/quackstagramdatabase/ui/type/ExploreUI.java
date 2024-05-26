package org.dacs.quackstagramdatabase.ui.type;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.DataManager;
import org.dacs.quackstagramdatabase.data.post.Post;
import org.dacs.quackstagramdatabase.data.post.PostManager;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.ui.UIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ExploreUI extends JFrame {
    private final UIUtil uiUtil;
    private final PostManager postManager;
    private final SearchUI searchUI;
    private final InstagramProfileUI profileUI;
    private final UIManager uiManager;

    private static final int IMAGE_SIZE = UIUtil.WIDTH / 3; // Size for each image in the grid

    @Autowired
    public ExploreUI(UIUtil uiUtil, PostManager postManager, SearchUI searchUI, InstagramProfileUI profileUI, UIManager uiManager) {
        this.uiUtil = uiUtil;
        this.postManager = postManager;
        this.searchUI = searchUI;
        this.profileUI = profileUI;
        this.uiManager = uiManager;

        setTitle("Explore");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();

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
        // Create the main content panel with search and image grid
        // Search bar at the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        //Click on the search bar to clear the default text
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setText("");
                searchUI.setVisible(true);
                uiManager.setCurrentFrame(searchUI);
                dispose(); // Close the current frame
            }
        });

        // Image Grid
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows


        List<Post> posts = postManager.getAsList();
        // Load images from the uploaded folder

        for (Post post : posts) {
            JLabel imageLabel = new JLabel(post.getImage(IMAGE_SIZE, IMAGE_SIZE));
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(post); // Call method to display the clicked image
                }
            });
            imageGridPanel.add(imageLabel);
        }

        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Main content panel that holds both the search bar and the image grid
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
        return mainContentPanel;
    }


    private JPanel createHeaderPanel() {

        // Header Panel (reuse from main.ui.type.InstagramProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(" Explore \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    private void displayImage(Post post) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Add the header and navigation panels back
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(uiUtil.createNavigationPanel(uiManager), BorderLayout.SOUTH);

        JPanel imageViewerPanel = new JPanel(new BorderLayout());

        // Calculate time since posting
        long days = ChronoUnit.DAYS.between(postManager.getWhenPosted(post), LocalDateTime.now());
        String timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";

        // Top panel for username and time since posting
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton usernameButton = new JButton(postManager.getPostedBy(post).getUsername());
        JLabel timeLabel = new JLabel(timeSincePosting);

        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameButton, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);


        // Prepare the image for display
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setIcon(post.getImage(IMAGE_SIZE, IMAGE_SIZE));

        // Bottom panel for bio and likes
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea bioTextArea = new JTextArea(post.getCaption());
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + postManager.getLikesCount(post));
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        // Adding the components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Re-add the header and navigation panels
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(uiUtil.createNavigationPanel(uiManager), BorderLayout.SOUTH);

        // Panel for the back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");

        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);
        topPanel.add(backButton);

        backButton.addActionListener(e -> {
            getContentPane().removeAll();
            add(createHeaderPanel(), BorderLayout.NORTH);
            add(createMainContentPanel(), BorderLayout.CENTER);
            add(uiUtil.createNavigationPanel(uiManager), BorderLayout.SOUTH);
            revalidate();
            repaint();
        });

        usernameButton.addActionListener(e -> {
            profileUI.setTemporaryUser(postManager.getPostedBy(post));
            profileUI.setVisible(true);
            this.uiManager.setCurrentFrame(profileUI);
            dispose(); // Close the current frame
        });

        // Container panel for image and details
        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the container panel and back button panel to the frame
//        add(backButtonPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
