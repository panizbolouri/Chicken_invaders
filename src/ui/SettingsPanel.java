package ui;

import javax.swing.*;
import java.awt.*;
import sound.SoundManager;

public class SettingsPanel extends JPanel {
    private Image bgImage;

    public SettingsPanel(JPanel mainPanel, CardLayout cardLayout) {
        bgImage = new ImageIcon("res/menu_bg.jpg").getImage();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        JLabel titleLabel = new JLabel("SETTINGS");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        //titleLabel.setForeground(new Color(0, 255, 255));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JButton musicButton = createSettingButton(SoundManager.isMusicEnabled() ? "MUSIC: ON" : "MUSIC: OFF");
        JButton shootButton = createSettingButton(SoundManager.isShootEnabled() ? "SHOOT SFX: ON" : "SHOOT SFX: OFF");
        JButton hitButton = createSettingButton(SoundManager.isHitEnabled() ? "HIT SFX: ON" : "HIT SFX: OFF");
        JButton endGameButton = createSettingButton(SoundManager.isEndGameEnabled() ? "END GAME: ON" : "END GAME: OFF");
        JButton backButton = createSettingButton("BACK TO MENU");

        gbc.gridy = 1; add(musicButton, gbc);
        gbc.gridy = 2; add(shootButton, gbc);
        gbc.gridy = 3; add(hitButton, gbc);
        gbc.gridy = 4; add(endGameButton, gbc);
        gbc.insets = new Insets(30, 15, 10, 15);
        gbc.gridy = 5; add(backButton, gbc);

        musicButton.addActionListener(e -> {
            boolean state = !SoundManager.isMusicEnabled();
            SoundManager.setMusicEnabled(state);
            musicButton.setText(state ? "MUSIC: ON" : "MUSIC: OFF");
            if (state) SoundManager.playMusic("res/music.wav");
        });

        shootButton.addActionListener(e -> {
            boolean state = !SoundManager.isShootEnabled();
            SoundManager.setShootEnabled(state);
            shootButton.setText(state ? "SHOOT SFX: ON" : "SHOOT SFX: OFF");
        });

        hitButton.addActionListener(e -> {
            boolean state = !SoundManager.isHitEnabled();
            SoundManager.setHitEnabled(state);
            hitButton.setText(state ? "HIT SFX: ON" : "HIT SFX: OFF");
        });

        endGameButton.addActionListener(e -> {
            boolean state = !SoundManager.isEndGameEnabled();
            SoundManager.setEndGameEnabled(state);
            endGameButton.setText(state ? "END GAME: ON" : "END GAME: OFF");
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
    }

    private JButton createSettingButton(String text) {
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
        button.setFont(new Font("Monospaced", Font.BOLD, 18));
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