package org.dacs.quackstagramdatabase.ui.type;

import lombok.Setter;
import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.data.user.UserManager;
import org.dacs.quackstagramdatabase.ui.UI;
import org.dacs.quackstagramdatabase.ui.UIManager;
import org.dacs.quackstagramdatabase.ui.UIUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class SignInUI extends JFrame {

    private final UserManager userManager;

    @Setter
    private UIManager uiManager;


    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    private JLabel lblPhoto;


    public SignInUI(UserManager userManager) {
        this.userManager = userManager;
        setTitle("Quackstagram - Register");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    public void initializeUI(InstagramProfileUI profileUI) {
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel("Quackstagram \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        // Profile picture placeholder without border
        setLabelPhoto();
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        // Text fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        setUpFieldsPanel(fieldsPanel, photoPanel);


        // Register button with black text
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(e -> {
            this.onSignInClicked(e, profileUI);
        });
        btnSignIn.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnSignIn.setForeground(Color.BLACK); // Set the text color to black
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnSignIn, BorderLayout.CENTER);

        // Adding components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);

        addRegisterButton();

        JPanel buttonPanel = getButtonPanel();

        // Adding the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void setUpFieldsPanel(JPanel fieldsPanel, JPanel photoPanel) {
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
    }

    private void setLabelPhoto() {
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("src/main/java/org/dacs/quackstagramdatabase/img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
    }

    private JPanel getButtonPanel() {
        // Panel to hold both buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);
        return buttonPanel;
    }

    private void addRegisterButton() {
        // New button for navigating to main.ui.type.SignUpUI
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(e -> this.uiManager.display("sign_up"));
        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);
    }

    private void onSignInClicked(ActionEvent event, InstagramProfileUI profileUI) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();

        System.out.println(enteredUsername + " <-> " + enteredPassword);

        User user = userManager.auth(enteredUsername, enteredPassword);

        if (user != null) {
            System.out.println("User authentication succeeded.");

            // If login worked then open the main page
            dispose();

            SwingUtilities.invokeLater(() -> {
                profileUI.setCurrentUser(user);
                profileUI.setVisible(true);
                this.uiManager.setCurrentFrame(profileUI);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
        }
    }

}
