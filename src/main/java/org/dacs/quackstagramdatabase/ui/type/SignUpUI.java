package org.dacs.quackstagramdatabase.ui.type;

import lombok.Getter;
import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.database.DatabaseConfig;
import org.dacs.quackstagramdatabase.database.EntityManager;
import org.dacs.quackstagramdatabase.database.entities.CredentialEntity;
import org.dacs.quackstagramdatabase.database.entities.UserEntity;
import org.dacs.quackstagramdatabase.ui.UI;
import org.dacs.quackstagramdatabase.ui.UIUtil;
import org.dacs.quackstagramdatabase.util.Util;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dacs.quackstagramdatabase.ui.UIManager;

@Component
public class SignUpUI extends JFrame {
    private final UserManager userManager;
    private final UIManager uiManager;

    private JTextField usernameText;
    private JTextField passwordText;
    private JTextField bioText;
    private JButton registerButton;
    private JLabel photoLabel;
    private JButton photoUploadButton;
    private JButton signInButton;

    private User registeredUser;
    @Getter
    @Setter
    private File possiblePicture;


    public SignUpUI(UserManager userManager, UIManager uiManager) {
        this.userManager = userManager;
        this.uiManager = uiManager;
        setTitle("Quackstagram - Register");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private JPanel createHeaderPanel() {
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel("Quackstagram \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    private JPanel profilePicturePlaceholder() {
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(80, 80));
        photoLabel.setHorizontalAlignment(JLabel.CENTER);
        photoLabel.setVerticalAlignment(JLabel.CENTER);
        photoLabel.setIcon(new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(photoLabel);
        return photoPanel;
    }

    private void initializeUI() {
        // Header with the Register label
        JPanel headerPanel = createHeaderPanel();

        // Profile picture placeholder without border
        JPanel photoPanel = profilePicturePlaceholder();

        // Text fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        initializeCredentials();

        setUpFieldsPanel(fieldsPanel, photoPanel);
        fieldsPanel.add(bioText);

        photoUploadButton = new JButton("Upload Photo");
        photoUploadButton.addActionListener(e -> handleProfilePictureUpload(false));
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(photoUploadButton);
        fieldsPanel.add(photoUploadPanel);

        addRegisterButton();

        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(registerButton, BorderLayout.CENTER);

        // Adding components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);
         // Adding the sign in button to the register panel or another suitable panel
        signInButton = new JButton("Already have an account? Sign In");

        signInButton.addActionListener(e -> {
            uiManager.display("sign_in");
        });

        registerPanel.add(signInButton, BorderLayout.SOUTH);
    }

    private void setUpFieldsPanel(JPanel fieldsPanel, JPanel photoPanel) {
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(usernameText);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(passwordText);
        fieldsPanel.add(Box.createVerticalStrut(10));
    }

    private void initializeCredentials() {
        usernameText = new JTextField("Username");
        passwordText = new JTextField("Password");
        bioText = new JTextField("Bio");
        bioText.setForeground(Color.GRAY);
        usernameText.setForeground(Color.GRAY);
        passwordText.setForeground(Color.GRAY);
    }

    private void addRegisterButton() {
        // Register button with black text
        registerButton = new JButton("Register");
        registerButton.addActionListener(this::onRegisterClicked);
        registerButton.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        registerButton.setForeground(Color.BLACK); // Set the text color to black
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
    }


    private void onRegisterClicked(ActionEvent event) {
        String username = usernameText.getText();
        String password = passwordText.getText();
        String bio = bioText.getText();

        if (userManager.exists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (getPossiblePicture() == null) {
            JOptionPane.showMessageDialog(this, "Please upload a photo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pfp_extention = Handler.getUtil().getFileExtension(getPossiblePicture());
        registeredUser = userManager.registerUser(username, password, bio, pfp_extention);
        handleProfilePictureUpload(true);

        this.uiManager.display("sign_in");
    }

    // Method to handle profile picture upload
     private void handleProfilePictureUpload(boolean infoCompleted) {
         if (!infoCompleted) {
             JFileChooser fileChooser = new JFileChooser();
             FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
             fileChooser.setFileFilter(filter);

             if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                 File selectedFile = fileChooser.getSelectedFile();

                 setPossiblePicture(selectedFile);

             }
         } else registeredUser.setProfilePicture(getPossiblePicture());
     }



}
