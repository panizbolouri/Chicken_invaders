package model;

import javax.swing.*;
import java.awt.*;

public class BossEnemy extends Enemy {
    private Image image;
    private int maxHealth;
    private int direction = 1;
    private int animFrame = 0;

    public BossEnemy(int x, int y, int health) {
        super(x, y, health, 500);
        this.maxHealth = health;
        this.width = 200;
        this.height = 200;
        this.image = new ImageIcon("res/boss1.png").getImage();
    }

    @Override
    public void move(int dx, int dy) {
        this.x += direction * 2;
        this.y = 50 + (int)(Math.sin(animFrame * 0.05) * 20);
        animFrame++;

        if (this.x <= 0 || this.x >= 800 - width) {
            direction *= -1;
        }
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

    public int getMaxHealth() { return maxHealth; }
}