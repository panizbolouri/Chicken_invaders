package model;

import javax.swing.*;

public class BossLevel4 extends Boss {
    private int direction = 1;
    private int animFrame = 0;

    public BossLevel4(int x, int y, int health) {
        super(x, y, health, 500);
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
}