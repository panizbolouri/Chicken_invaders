package model;

import javax.swing.*;
import java.awt.*;

public class Egg {
    private double x, y;
    private double dx, dy;
    private Image image;

    public Egg(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.image = new ImageIcon("res/egg.png").getImage();
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.drawImage(image, (int)x, (int)y, 35, 50, null);
    }

    public boolean isOutOfBounds() {
        return y > 600 || y < -50 || x < -50 || x > 800;
    }

    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}