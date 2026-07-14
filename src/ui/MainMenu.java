package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("CHICKEN INVADERS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton newGameButton = createMenuButton("New Game", new Color(76, 175, 80), bgColor);
        JButton highScoresButton = createMenuButton("High Scores", new Color(33, 150, 243), bgColor);
        JButton settingsButton = createMenuButton("Settings", new Color(255, 152, 0), bgColor);
        JButton howToPlayButton = createMenuButton("How to Play", new Color(156, 39, 176), bgColor);
        JButton exitButton = createMenuButton("Exit", new Color(244, 67, 54), bgColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 50, 10);
        add(titleLabel, gbc);

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 1; add(newGameButton, gbc);
        gbc.gridy = 2; add(highScoresButton, gbc);
        gbc.gridy = 3; add(settingsButton, gbc);
        gbc.gridy = 4; add(howToPlayButton, gbc);
        gbc.gridy = 5; add(exitButton, gbc);

        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "GamePage"));
        highScoresButton.addActionListener(e -> cardLayout.show(mainPanel, "LeaderboardPage"));
        howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlayPage"));
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "SettingsPage"));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createMenuButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 18));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        return button;
    }
}