package model;

import javax.swing.*;
import java.awt.*;

public class PowerUp {
    private int x, y;
    private int speed = 2;
    private int type;
    // 0: Add Fire, 1: Rapid Fire, 2: Extra Life, 3: Shield, 4: Freeze
    private Image image;

    public PowerUp(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.image = new ImageIcon("res/powerup_" + type + ".png").getImage();
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, 40, 40, null);
    }

    public boolean isOutOfBounds() {
        return y > 600;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getType() { return type; }
}