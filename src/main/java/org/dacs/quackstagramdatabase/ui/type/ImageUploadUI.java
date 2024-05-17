package org.dacs.quackstagramdatabase.ui.type;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.picture.Picture;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.ui.UIUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ImageUploadUI extends JFrame {

    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = UIUtil.WIDTH / 3; // Static size for grid images
    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;
    private JButton saveButton;

    public ImageUploadUI() {
        setTitle("Upload Image");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel(); // Reuse the createHeaderPanel method
        JPanel navigationPanel = UIUtil.createNavigationPanel(); // Reuse the createNavigationPanel method

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image preview
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Bio text area
        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);

        // Upload button
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        // Save button (for bio)
        saveButton = new JButton("Save Caption");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(this::saveBioAction);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }


    private void uploadAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            User user = Handler.getDataManager().forUsers().getCurrentUser();
            Picture picture = new Picture(
                    UUID.randomUUID(),
                    user.getUuid(),
                    bioTextArea.getText(),
                    "",
                    new ArrayList<>(),
                    new HashMap<>())
                    .uploadImage(selectedFile);

            Handler.getDataManager().forPictures().postPicture(
                    user, picture);

            ImageIcon imageIcon = picture.getImage(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE);

            // Check if imagePreviewLabel has a valid size
            if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                Image image = imageIcon.getImage();

                // Calculate the dimensions for the image preview
                int previewWidth = imagePreviewLabel.getWidth();
                int previewHeight = imagePreviewLabel.getHeight();
                int imageWidth = image.getWidth(null);
                int imageHeight = image.getHeight(null);
                double widthRatio = (double) previewWidth / imageWidth;
                double heightRatio = (double) previewHeight / imageHeight;
                double scale = Math.min(widthRatio, heightRatio);
                int scaledWidth = (int) (scale * imageWidth);
                int scaledHeight = (int) (scale * imageHeight);

                // Set the image icon with the scaled image
                imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
            }

            imagePreviewLabel.setIcon(imageIcon);


            // Change the text of the upload button
            uploadButton.setText("Upload Another Image");
            JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
        }
    }

    private void saveBioAction(ActionEvent event) {
        // Here you would handle saving the bio text
        String bioText = bioTextArea.getText();
        // For example, save the bio text to a file or database
        JOptionPane.showMessageDialog(this, "Caption saved: " + bioText);
    }

    private JPanel createHeaderPanel() {
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel(" Upload Image \u1F425");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

}
