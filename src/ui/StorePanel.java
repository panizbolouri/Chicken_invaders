package ui;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StorePanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel coinsLabel;
    private JPanel planesContainer;
    private final Color GALAXY_BG = new Color(15, 10, 35);
    private final Color CARD_BG = new Color(30, 20, 60);
    private final Color ACCENT_PURPLE = new Color(138, 43, 226);

    public StorePanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
        setBackground(GALAXY_BG);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GALAXY_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel titleLabel = new JLabel("HANGAR / STORE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 255, 255));

        coinsLabel = new JLabel("Coins: 0");
        coinsLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        coinsLabel.setForeground(new Color(255, 215, 0));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(coinsLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        planesContainer = new JPanel(new GridLayout(2, 2, 15, 15));
        planesContainer.setBackground(GALAXY_BG);
        planesContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(planesContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(GALAXY_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton backBtn = new JButton("Back to Main Menu");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 20));
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(300, 45));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                refreshStore();
            }
        });
    }

    private void refreshStore() {
        String user = LoginPanel.loggedInUser;
        if (user == null || user.isEmpty()) return;

        int coins = DatabaseManager.getCoins(user);
        coinsLabel.setText("Coins: " + coins + "  ");
        List<String> unlocked = DatabaseManager.getUnlockedPlanes(user);
        String equipped = DatabaseManager.getEquippedPlane(user);

        planesContainer.removeAll();

        addPlaneCard("Default", 0, "<html><b style='color:lime;'>Cost: FREE</b><br>Speed: 5<br>Fire Rate: 300ms<br>Lives: 3<br>Special: -</html>", coins, unlocked, equipped, user, "res/plane.png");
        addPlaneCard("Fast", 5000, "<html><b style='color:orange;'>Cost: 5,000</b><br>Speed: 7<br>Fire Rate: 250ms<br>Lives: 3<br>Special: -</html>", coins, unlocked, equipped, user, "res/fastplane.png");
        addPlaneCard("Heavy", 8000, "<html><b style='color:orange;'>Cost: 8,000</b><br>Speed: 4<br>Fire Rate: 200ms<br>Lives: 5<br>Special: -</html>", coins, unlocked, equipped, user, "res/heavyplane.png");
        addPlaneCard("Sniper", 10000, "<html><b style='color:orange;'>Cost: 10,000</b><br>Speed: 5<br>Fire Rate: 150ms<br>Lives: 3<br>Special: 2X Boss Dmg</html>", coins, unlocked, equipped, user, "res/sniperplane.png");

        planesContainer.revalidate();
        planesContainer.repaint();
    }

    private void addPlaneCard(String name, int cost, String desc, int coins, List<String> unlocked, String equipped, String user, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 2));
        card.setBackground(CARD_BG);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        nameLabel.setForeground(Color.CYAN);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel centerPanel = new JPanel(new BorderLayout(15, 0));
        centerPanel.setBackground(CARD_BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));

        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
            centerPanel.add(imgLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        descLabel.setForeground(Color.WHITE);
        centerPanel.add(descLabel, BorderLayout.CENTER);

        JButton actionBtn = new JButton();
        actionBtn.setFont(new Font("Monospaced", Font.BOLD, 16));
        actionBtn.setFocusPainted(false);
        actionBtn.setOpaque(true);
        actionBtn.setBorderPainted(false);

        if (equipped.equals(name)) {
            actionBtn.setText("EQUIPPED");
            actionBtn.setBackground(new Color(50, 205, 50));
            actionBtn.setForeground(Color.BLACK);
            actionBtn.setEnabled(false);
        } else if (unlocked.contains(name)) {
            actionBtn.setText("SELECT");
            actionBtn.setBackground(new Color(30, 144, 255));
            actionBtn.setForeground(Color.WHITE);
            actionBtn.addActionListener(e -> {
                DatabaseManager.setEquippedPlane(user, name);
                refreshStore();
            });
        } else {
            if (coins >= cost) {
                actionBtn.setText("BUY (" + cost + ")");
                actionBtn.setBackground(new Color(255, 140, 0));
                actionBtn.setForeground(Color.BLACK);
                actionBtn.addActionListener(e -> {
                    DatabaseManager.buyPlane(user, name, cost);
                    refreshStore();
                });
            } else {
                actionBtn.setText("NOT ENOUGH COINS");
                actionBtn.setBackground(new Color(80, 80, 80));
                actionBtn.setForeground(new Color(200, 200, 200));
                actionBtn.setEnabled(false);
            }
        }

        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBackground(CARD_BG);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        btnPanel.add(actionBtn, BorderLayout.CENTER);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        planesContainer.add(card);
    }
}