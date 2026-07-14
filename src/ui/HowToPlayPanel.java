package ui;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    public HowToPlayPanel(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new BorderLayout(10, 10));

        setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        Font titleFont = new Font("Monospaced", Font.BOLD, 36);
        Font headerFont = new Font("Monospaced", Font.BOLD, 18);
        Font cellFont = new Font("Monospaced", Font.BOLD, 15);

        JLabel titleLabel = new JLabel("GAME CONTROLS");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(bgColor);

        JPanel tablePanel = new JPanel(new GridLayout(9, 2));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(156, 39, 176), 3));
        tablePanel.setBackground(bgColor);

        tablePanel.add(createCell("Key / Control", true, false, headerFont));
        tablePanel.add(createCell("Action / Function", true, false, headerFont));

        String[][] data = {
                {"→ / D", "Move Right"},
                {"← / A", "Move Left"},
                {"↑ / W", "Move Up"},
                {"↓ / S", "Move Down"},
                {"Space", "Shoot Laser"},
                {"P", "Pause / Resume"},
                {"Esc", "Exit to Menu"},
                {"M", "Sound Settings"}
        };

        for (int i = 0; i < data.length; i++) {
            boolean isAlt = (i % 2 == 0);
            tablePanel.add(createCell(data[i][0], false, isAlt, cellFont));
            tablePanel.add(createCell(data[i][1], false, isAlt, cellFont));
        }

        centerContainer.add(tablePanel);
        add(centerContainer, BorderLayout.CENTER);

        JButton backButton = new JButton("BACK TO MENU");
        backButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        backButton.setBackground(new Color(217, 83, 79));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(250, 45));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createCell(String text, boolean isHeader, boolean isAlt, Font font) {
        JPanel cell = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);

        if (isHeader) {
            cell.setBackground(new Color(156, 39, 176));
            label.setForeground(Color.WHITE);
            cell.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        } else {
            cell.setBackground(isAlt ? new Color(50, 54, 62) : new Color(60, 64, 72));
            label.setForeground(new Color(230, 230, 230));
            cell.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        }

        cell.add(label, BorderLayout.CENTER);
        return cell;
    }
}