package ui;

import database.DatabaseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class LeaderboardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public LeaderboardPanel(JPanel mainPanel, CardLayout cardLayout) {
        Color bgColor = new Color(40, 44, 52);
        setBackground(bgColor);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("TOP 10 PILOTS");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Rank", "Pilot Name", "High Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setBackground(new Color(50, 54, 62));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(76, 175, 80));
        table.getTableHeader().setForeground(bgColor);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        backButton.setBackground(new Color(217, 83, 79));
        backButton.setForeground(bgColor);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshScores();
            }
        });
    }

    private void refreshScores() {
        tableModel.setRowCount(0);
        ArrayList<String[]> topScores = DatabaseManager.getTopScores();
        int rank = 1;
        for (String[] scoreData : topScores) {
            tableModel.addRow(new Object[]{rank++, scoreData[0], scoreData[1]});
        }
    }
}