import database.DatabaseManager;
import ui.*;

import javax.swing.*;
import java.awt.*;

public class GameMain {
    public static void main(String[] args) {
        DatabaseManager.initialize();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chicken Invaders");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            LoginPanel loginPanel = new LoginPanel(mainPanel, cardLayout);
            RegisterPanel registerPanel = new RegisterPanel(mainPanel, cardLayout);
            MainMenu mainMenuPanel = new MainMenu(mainPanel, cardLayout);
            GamePanel gamePanel = new GamePanel(mainPanel, cardLayout);
            LeaderboardPanel leaderboardPanel = new LeaderboardPanel(mainPanel, cardLayout);
            HowToPlayPanel howToPlayPanel = new HowToPlayPanel(mainPanel, cardLayout);
            SettingsPanel settingsPanel = new SettingsPanel(mainPanel, cardLayout);
            StorePanel storePanel = new StorePanel(mainPanel, cardLayout);

            mainPanel.add(loginPanel, "LoginPage");
            mainPanel.add(registerPanel, "RegisterPage");
            mainPanel.add(mainMenuPanel, "MainMenu");
            mainPanel.add(gamePanel, "GamePage");
            mainPanel.add(leaderboardPanel, "LeaderboardPage");
            mainPanel.add(howToPlayPanel, "HowToPlayPage");
            mainPanel.add(settingsPanel, "SettingsPage");
            mainPanel.add(storePanel, "StorePage");

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}