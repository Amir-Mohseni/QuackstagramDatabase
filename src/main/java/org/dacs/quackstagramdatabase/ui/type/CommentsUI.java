package org.dacs.quackstagramdatabase.ui.type;

import org.dacs.quackstagramdatabase.Handler;
import org.dacs.quackstagramdatabase.data.post.Post;
import org.dacs.quackstagramdatabase.data.user.User;
import org.dacs.quackstagramdatabase.database.entities.CommentEntity;
import org.dacs.quackstagramdatabase.ui.UIUtil;

import javax.swing.*;
import java.awt.*;



public class CommentsUI extends JFrame {
    Post post;
    JPanel contentPanel; // Declare contentPanel as a class-level variable to access it later

    public CommentsUI(Post post) {
        this.post = post;

        setTitle("Comments");
        setSize(UIUtil.WIDTH, UIUtil.HEIGHT);
        setMinimumSize(new Dimension(UIUtil.WIDTH, UIUtil.HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel navigationPanel = UIUtil.createNavigationPanel();

        // Content Panel for comments
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add comments to the panel
        updateCommentsPanel();

        // Add a text field and a button to add new comments
        JPanel addCommentPanel = getAddCommentPanel();

        // Add the content panel and the add comment panel to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(addCommentPanel, BorderLayout.SOUTH);

        // Add the header and navigation panel to the frame
        add(navigationPanel, BorderLayout.NORTH);


        revalidate();
        repaint();
    }

    private void updateCommentsPanel() {
        contentPanel.removeAll(); // Clear existing comments
        //Iterate over the comments and add them to the panel key and value
        for (CommentEntity comment: post.getComments()) {
            String commentText = comment.getUsername() + ": " + comment.getCommentText();
            JLabel commentLabel = new JLabel(commentText);
            commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            contentPanel.add(commentLabel);
        }
    }

    private JPanel getAddCommentPanel() {
        // Panel to add a comment
        JPanel addCommentPanel = new JPanel(new BorderLayout());
        JTextField commentField = new JTextField();
        JButton addCommentButton = new JButton("Add Comment");
        addCommentButton.addActionListener(e -> {
            String comment = commentField.getText();
            if (!comment.isEmpty()) {
                User currentUser = Handler.getDataManager().forUsers().getCurrentUser();
                post.addComment(currentUser, comment);
                // Update the content panel with the new comment
                String commentText = currentUser.getUsername() + ": " + comment;
                JLabel newCommentLabel = new JLabel(commentText);
                newCommentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                contentPanel.add(newCommentLabel);

                // Repaint the UI to reflect the changes
                revalidate();
                repaint();
            }
        });
        addCommentPanel.add(commentField, BorderLayout.CENTER);
        addCommentPanel.add(addCommentButton, BorderLayout.EAST);
        return addCommentPanel;
    }

}
