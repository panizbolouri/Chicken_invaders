package ui;

import javax.swing.*;
import java.awt.*;
import manager.SoundManager;

public class LoginPanel extends JPanel {

    public static String loggedInUser = "";

    public LoginPanel(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("CHICKEN INVADERS!");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // ------------------------------------

        Font labelFont = new Font("Tahoma", Font.BOLD, 14);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 14);
        Color textColor = Color.WHITE;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        userLabel.setFont(labelFont);
        JTextField userField = new JTextField(15);
        userField.setFont(fieldFont);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(textColor);
        passLabel.setFont(labelFont);
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(fieldFont);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(bgColor);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 45));

        JLabel noAccountLabel = new JLabel("Don't have an account? Register NOW!");
        noAccountLabel.setForeground(new Color(200, 200, 200));
        noAccountLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        noAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(bgColor);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 35));

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // قرار دادن تایتل‌ها در بالای صفحه
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 0, 10); // فاصله کمتر در پایین
        add(welcomeLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 30, 10); // فاصله بیشتر در پایین تا فرم ورود
        add(titleLabel, gbc);

        // برگرداندن تنظیمات به حالت اولیه برای فیلدهای یوزرنیم و پسورد
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2; add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(passField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(25, 10, 10, 10);
        add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 5, 10);
        add(noAccountLabel, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (manager.DatabaseManager.loginUser(user, pass)) {
                loggedInUser = user;
                JOptionPane.showMessageDialog(this, "Login Successful!");
                SoundManager.playMusic("res/music.wav");
                cardLayout.show(mainPanel, "MainMenu");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "RegisterPage"));
    }
}
