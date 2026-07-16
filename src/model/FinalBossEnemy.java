package model;

import javax.swing.*;
import java.awt.*;

public class FinalBossEnemy extends Enemy {
    private Image image;
    private int maxHealth;
    private double angle = 0;
    private int startY;

    public FinalBossEnemy(int x, int y, int health) {
        super(x, y, health, 2000);
        this.maxHealth = health;
        this.width = 250;
        this.height = 250;
        this.image = new ImageIcon("res/boss2.png").getImage();
        this.startY = y;
    }

    @Override
    public void move(int dx, int dy) {
        angle += 0.02;
        this.x = (int)(800 / 2 - width / 2 + Math.sin(angle) * 200);
        this.y = startY + (int)(Math.sin(angle * 2.5) * 50);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);

        g.setColor(Color.RED);
        g.fillRect(x, y - 15, width, 10);

        g.setColor(Color.GREEN);
        int healthBarWidth = (int)((double)health / maxHealth * width);
        g.fillRect(x, y - 15, healthBarWidth, 10);

        g.setColor(Color.WHITE);
        g.drawRect(x, y - 15, width, 10);
    }
}