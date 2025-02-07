package librarysystem.ui.auth;

import business.SystemController;
import business.LoginException;
import dataaccess.Auth;
import librarysystem.ui.Dashboard;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame { // Handles Authentication Logic
    private final LoginPanel loginPanel;
    private final SystemController systemController;

    public LoginFrame() {
        setTitle("Library Login");
        setSize(350, 400); // Increased height for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        systemController = new SystemController(); // System logic
        loginPanel = new LoginPanel(); // UI Panel

        loginPanel.loginButton.addActionListener(e -> authenticate());

        // Load and resize the image
        ImageIcon icon = new ImageIcon("src/main/resources/library.jpg"); // Change path accordingly
        Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH); // Resize to fit
        ImageIcon resizedIcon = new ImageIcon(img);

        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Panel for the image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new FlowLayout());
        imagePanel.add(imageLabel);

        // Create a panel to hold everything
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imagePanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void authenticate() {
        String username = loginPanel.usernameField.getText().trim();
        String password = new String(loginPanel.passwordField.getPassword()).trim();

        try {
            systemController.login(username, password);
            Auth role = SystemController.currentAuth;

            if (role == null) { // Check if role is null
                JOptionPane.showMessageDialog(this, "Authentication failed. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            new Dashboard(role.name()); // Open dashboard based on role
        } catch (LoginException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
