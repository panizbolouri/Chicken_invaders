package model;

import java.awt.*;

public abstract class Boss extends Enemy {
    protected Image image;
    protected int maxHealth;

    public Boss(int x, int y, int health, int scoreValue) {
        super(x, y, health, scoreValue);
        this.maxHealth = health;
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }

        g.setColor(Color.RED);
        g.fillRect(x, y - 15, width, 10);

        g.setColor(Color.GREEN);
        int healthBarWidth = (int)((double)health / maxHealth * width);
        g.fillRect(x, y - 15, healthBarWidth, 10);

        g.setColor(Color.WHITE);
        g.drawRect(x, y - 15, width, 10);
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}