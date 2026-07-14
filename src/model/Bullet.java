package model;

import javax.swing.*;
import java.awt.*;

public class Bullet {
    private int x, y;
    private int speed = 8;
    private Image image;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon("res/bullet.png").getImage();
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 20, 60, null);
    }

    public boolean isOutOfBounds() {
        return y < 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}