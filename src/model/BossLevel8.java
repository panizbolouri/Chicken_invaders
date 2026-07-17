package model;

import javax.swing.*;

public class BossLevel8 extends Boss {
    private double angle = 0;
    private int startY;

    public BossLevel8(int x, int y, int health) {
        super(x, y, health, 2000);
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
}