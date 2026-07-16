package model;

import javax.swing.*;
import java.awt.*;

public class ShooterEnemy extends Enemy {
    private Image image;

    public ShooterEnemy(int x, int y) {
        super(x, y, 2, 30);
        this.image = new ImageIcon("res/shooter_chicken.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}