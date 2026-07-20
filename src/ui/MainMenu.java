package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    private Image bgImage;

    public MainMenu(JPanel mainPanel, CardLayout cardLayout) {
        bgImage = new ImageIcon("res/menu_bg.jpg").getImage();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton newGameButton = createMenuButton("New Game");
        JButton storeButton = createMenuButton("Store");
        JButton highScoresButton = createMenuButton("High Scores");
        JButton settingsButton = createMenuButton("Settings");
        JButton howToPlayButton = createMenuButton("How to Play");
        JButton exitButton = createMenuButton("Exit");

        gbc.gridx = 0;
        gbc.gridy = 0; add(Box.createRigidArea(new Dimension(0, 120)), gbc);
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

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 20, 100, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0, 255, 255, 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(280, 50));
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}