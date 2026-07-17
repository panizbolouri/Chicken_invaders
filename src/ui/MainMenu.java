package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(JPanel mainPanel, CardLayout cardLayout) {
        Color galaxyBg = new Color(15, 10, 35);
        setBackground(galaxyBg);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("CHICKEN INVADERS");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 46));
        titleLabel.setForeground(new Color(0, 255, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton newGameButton = createMenuButton("New Game", new Color(50, 205, 50), Color.BLACK);
        JButton storeButton = createMenuButton("Store", new Color(255, 140, 0), Color.BLACK);
        JButton highScoresButton = createMenuButton("High Scores", new Color(138, 43, 226), Color.WHITE);
        JButton settingsButton = createMenuButton("Settings", new Color(138, 43, 226), Color.WHITE);
        JButton howToPlayButton = createMenuButton("How to Play", new Color(138, 43, 226), Color.WHITE);
        JButton exitButton = createMenuButton("Exit", new Color(220, 20, 60), Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 60, 10);
        add(titleLabel, gbc);

        gbc.insets = new Insets(10, 10, 15, 10);

        gbc.gridy = 1; add(newGameButton, gbc);
        gbc.gridy = 2; add(storeButton, gbc);
        gbc.gridy = 3; add(highScoresButton, gbc);
        gbc.gridy = 4; add(settingsButton, gbc);
        gbc.gridy = 5; add(howToPlayButton, gbc);
        gbc.gridy = 6; add(exitButton, gbc);

        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "GamePage"));
        highScoresButton.addActionListener(e -> cardLayout.show(mainPanel, "LeaderboardPage"));
        howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlayPage"));
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "SettingsPage"));
        storeButton.addActionListener(e -> cardLayout.show(mainPanel, "StorePage"));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createMenuButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 50));
        return button;
    }
}