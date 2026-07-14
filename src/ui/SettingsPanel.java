package ui;

import javax.swing.*;
import java.awt.*;
import sound.SoundManager;

public class SettingsPanel extends JPanel {
    private JButton musicButton;
    private JButton shootButton;
    private JButton hitButton;
    private JButton endGameButton;

    public SettingsPanel(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font titleFont = new Font("Monospaced", Font.BOLD, 36);
        Font buttonFont = new Font("Monospaced", Font.BOLD, 18);

        JLabel titleLabel = new JLabel("SETTINGS");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 15, 20, 15);
        add(titleLabel, gbc);

        gbc.insets = new Insets(8, 15, 8, 15);

        musicButton = createSettingButton(SoundManager.isMusicEnabled() ? "MUSIC: ON" : "MUSIC: OFF", buttonFont);
        shootButton = createSettingButton(SoundManager.isShootEnabled() ? "SHOOT SFX: ON" : "SHOOT SFX: OFF", buttonFont);
        hitButton = createSettingButton(SoundManager.isHitEnabled() ? "HIT SFX: ON" : "HIT SFX: OFF", buttonFont);
        endGameButton = createSettingButton(SoundManager.isEndGameEnabled() ? "END GAME: ON" : "END GAME: OFF", buttonFont);

        gbc.gridy = 1; add(musicButton, gbc);
        gbc.gridy = 2; add(shootButton, gbc);
        gbc.gridy = 3; add(hitButton, gbc);
        gbc.gridy = 4; add(endGameButton, gbc);

        JButton backButton = new JButton("BACK TO MENU");
        backButton.setFont(buttonFont);
        backButton.setBackground(new Color(217, 83, 79));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(280, 45));

        gbc.gridy = 5;
        gbc.insets = new Insets(25, 15, 15, 15);
        add(backButton, gbc);

        musicButton.addActionListener(e -> {
            boolean state = !SoundManager.isMusicEnabled();
            SoundManager.setMusicEnabled(state);
            musicButton.setText(state ? "MUSIC: ON" : "MUSIC: OFF");
            musicButton.setBackground(state ? new Color(76, 175, 80) : new Color(244, 67, 54));
            if (state) {
                SoundManager.playMusic("res/music.wav");
            }
        });

        shootButton.addActionListener(e -> {
            boolean state = !SoundManager.isShootEnabled();
            SoundManager.setShootEnabled(state);
            shootButton.setText(state ? "SHOOT SFX: ON" : "SHOOT SFX: OFF");
            shootButton.setBackground(state ? new Color(76, 175, 80) : new Color(244, 67, 54));
        });

        hitButton.addActionListener(e -> {
            boolean state = !SoundManager.isHitEnabled();
            SoundManager.setHitEnabled(state);
            hitButton.setText(state ? "HIT SFX: ON" : "HIT SFX: OFF");
            hitButton.setBackground(state ? new Color(76, 175, 80) : new Color(244, 67, 54));
        });

        endGameButton.addActionListener(e -> {
            boolean state = !SoundManager.isEndGameEnabled();
            SoundManager.setEndGameEnabled(state);
            endGameButton.setText(state ? "END GAME: ON" : "END GAME: OFF");
            endGameButton.setBackground(state ? new Color(76, 175, 80) : new Color(244, 67, 54));
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
    }

    private JButton createSettingButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        boolean isOn = text.contains("ON");
        button.setBackground(isOn ? new Color(76, 175, 80) : new Color(244, 67, 54));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(280, 45));
        return button;
    }
}