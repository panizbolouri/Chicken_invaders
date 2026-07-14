package ui;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Tahoma", Font.BOLD, 14);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 14);
        Color textColor = Color.WHITE;

        JLabel userLabel = new JLabel("New Username:");
        userLabel.setForeground(textColor);
        userLabel.setFont(labelFont);
        JTextField userField = new JTextField(15);
        userField.setFont(fieldFont);

        JLabel passLabel = new JLabel("New Password:");
        passLabel.setForeground(textColor);
        passLabel.setFont(labelFont);
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(fieldFont);

        JButton registerButton = new JButton("Submit");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(bgColor);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setPreferredSize(new Dimension(200, 45));

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        backButton.setForeground(Color.LIGHT_GRAY);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 0; add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(passField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        add(registerButton, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(backButton, gbc);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LoginPage"));

        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            if (DatabaseManager.registerUser(user, pass)) {
                JOptionPane.showMessageDialog(this, "Registration Successful! You can login now.");
                cardLayout.show(mainPanel, "LoginPage");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            }
        });
    }
}